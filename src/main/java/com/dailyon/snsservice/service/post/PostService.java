package com.dailyon.snsservice.service.post;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.cache.Top4OOTDRedisRepository;
import com.dailyon.snsservice.client.dto.CouponForProductResponse;
import com.dailyon.snsservice.client.dto.ProductInfoResponse;
import com.dailyon.snsservice.client.feign.ProductServiceClient;
import com.dailyon.snsservice.client.feign.PromotionServiceClient;
import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.dto.response.post.*;
import com.dailyon.snsservice.dto.response.postimageproductdetail.PostImageProductDetailResponse;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.dailyon.snsservice.mapper.hashtag.HashTagMapper;
import com.dailyon.snsservice.mapper.post.PostMapper;
import com.dailyon.snsservice.mapper.postimage.PostImageMapper;
import com.dailyon.snsservice.mapper.postimageproductdetail.PostImageProductDetailMapper;
import com.dailyon.snsservice.repository.post.PostRepository;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.s3.S3Service;
import com.dailyon.snsservice.vo.PostCountVO;
import com.dailyon.snsservice.vo.Top4OOTDVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

  private static final String STATIC_IMG_BUCKET = "dailyon-static-dev";
  private static final String POST_STATIC_IMG_BUCKET_PREFIX = "post-img";

  private final PostRepository postRepository;
  private final PostCountRedisRepository postCountRedisRepository;
  private final Top4OOTDRedisRepository top4OOTDRedisRepository;
  private final PostImageProductDetailMapper postImageProductDetailMapper;
  private final PostMapper postMapper;
  private final PostImageMapper postImageMapper;
  private final HashTagMapper hashTagMapper;
  private final MemberReader memberReader;
  private final S3Service s3Service;
  private final ProductServiceClient productServiceClient;
  private final PromotionServiceClient promotionServiceClient;

  public PostPageResponse getPosts(Long memberId, Pageable pageable) {
    Page<PostResponse> postResponses = postRepository.findAllWithIsLike(memberId, pageable);

    List<PostResponse> modifiedPostResponses = new ArrayList<>();

    postResponses
        .getContent()
        .forEach(
            postResponse -> {
              try {
                PostCountVO dbPostCountVO =
                    new PostCountVO(
                        postResponse.getViewCount(),
                        postResponse.getLikeCount(),
                        postResponse.getCommentCount());

                // get count from cache or add all counts to cache
                PostCountVO cachedPostCountVO =
                    postCountRedisRepository.findOrPutPostCountVO(
                        String.valueOf(postResponse.getId()), dbPostCountVO);

                // cache count 값으로 response를 업데이트
                postResponse.setViewCount(cachedPostCountVO.getViewCount());
                postResponse.setLikeCount(cachedPostCountVO.getLikeCount());

                // Add the modified PostResponse to the list
                modifiedPostResponses.add(postResponse);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });

    // TODO: 정렬 기준은 하나로 고정
    List<String> sortProperties = getSortProperties(pageable);
    if (sortProperties.get(0).equals("viewCount")) {
      modifiedPostResponses.sort(Comparator.comparing(PostResponse::getViewCount).reversed());
    } else if (sortProperties.get(0).equals("likeCount")) {
      modifiedPostResponses.sort(Comparator.comparing(PostResponse::getLikeCount).reversed());
    }

    PostPageResponse sortedPostPageResponse =
        PostPageResponse.fromDto(
            new PageImpl<>(
                modifiedPostResponses,
                postResponses.getPageable(),
                postResponses.getTotalElements()));

    return sortedPostPageResponse;
  }

  @Transactional
  public CreatePostResponse createPost(Long memberId, CreatePostRequest createPostRequest) {
    String thumbnailImgUrl =
        String.format(
            "/%s/%s.%s",
            POST_STATIC_IMG_BUCKET_PREFIX,
            UUID.randomUUID(),
            createPostRequest.getPostThumbnailImgName().split("\\.")[1]);
    String imgUrl =
        String.format(
            "/%s/%s.%s",
            POST_STATIC_IMG_BUCKET_PREFIX,
            UUID.randomUUID(),
            createPostRequest.getPostImgName().split("\\.")[1]);

    // 게시글 엔티티 생성
    Member member = memberReader.read(memberId);
    Set<PostImageProductDetail> postImageProductDetails =
        postImageProductDetailMapper.createPostImageProductDetails(
            createPostRequest.getPostImageProductDetails());
    PostImage postImage =
        postImageMapper.createPostImage(thumbnailImgUrl, imgUrl, postImageProductDetails);
    List<HashTag> hashTags = hashTagMapper.createHashTags(createPostRequest.getHashTagNames());
    Post post = postMapper.createPost(createPostRequest, member, postImage, hashTags);

    Post savedPost = postRepository.save(post);

    String thumbnailImgPreSignedUrl =
        s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, thumbnailImgUrl.substring(1));
    String imgPreSignedUrl = s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, imgUrl.substring(1));

    return CreatePostResponse.builder()
        .id(savedPost.getId())
        .thumbnailImgPreSignedUrl(thumbnailImgPreSignedUrl)
        .imgPreSignedUrl(imgPreSignedUrl)
        .build();
  }

  @Transactional
  public UpdatePostResponse updatePost(
      Long id, Long memberId, UpdatePostRequest updatePostRequest) {
    Post post = postRepository.findByIdAndMemberIdForUpdate(id, memberId);
    post.updatePostAndPostImageProductDetail(updatePostRequest);

    String thumbnailImgPreSignedUrl =
        s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, post.getPostImage().getThumbnailImgUrl());
    String imgPreSignedUrl =
        s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, post.getPostImage().getImgUrl());
    return UpdatePostResponse.builder()
        .id(id)
        .thumbnailImgPreSignedUrl(thumbnailImgPreSignedUrl)
        .imgPreSignedUrl(imgPreSignedUrl)
        .build();
  }

  @Transactional
  public void softDeletePost(Long id, Long memberId) {
    postRepository.softDeleteById(id, memberId);
    postCountRedisRepository.deletePostCountVO(String.valueOf(id));
  }

  public PostLikePageResponse getPostLikes(Long memberId, Pageable pageable) {
    Page<Post> posts = postRepository.findAllWithPostLikeByMemberIdIn(memberId, pageable);

    List<Post> modifiedPosts = new ArrayList<>();

    posts
        .forEach(
            post -> {
              try {
                PostCountVO dbPostCountVO =
                    new PostCountVO(
                        post.getViewCount(), post.getLikeCount(), post.getCommentCount());

                // get count from cache or add all counts to cache
                PostCountVO cachedPostCountVO =
                    postCountRedisRepository.findOrPutPostCountVO(
                        String.valueOf(post.getId()), dbPostCountVO);

                // cache count 값으로 response를 업데이트
                post.setViewCount(cachedPostCountVO.getViewCount());
                post.setLikeCount(cachedPostCountVO.getLikeCount());

                modifiedPosts.add(post);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });

    // TODO: 정렬 기준은 하나로 고정
    List<String> sortProperties = getSortProperties(pageable);
    if (sortProperties.get(0).equals("viewCount")) {
      modifiedPosts.sort(Comparator.comparing(Post::getViewCount).reversed());
    } else if (sortProperties.get(0).equals("likeCount")) {
      modifiedPosts.sort(Comparator.comparing(Post::getLikeCount).reversed());
    }

    PostLikePageResponse sortedPostPageResponse =
            PostLikePageResponse.fromEntity(
                    new PageImpl<>(
                            modifiedPosts,
                            posts.getPageable(),
                            posts.getTotalElements()));

    return sortedPostPageResponse;
  }

  public OOTDPostPageResponse getMyOOTDPosts(Long memberId, Pageable pageable) {
    Page<OOTDPostResponse> myOOTDPostResponses =
        postRepository.findMyPostsByMemberId(memberId, pageable);

    List<OOTDPostResponse> modifiedMyOOTDPostResponses = new ArrayList<>();

    myOOTDPostResponses
        .getContent()
        .forEach(
            OOTDPostResponse -> {
              try {
                PostCountVO dbPostCountVO =
                    new PostCountVO(
                        OOTDPostResponse.getViewCount(),
                        OOTDPostResponse.getLikeCount(),
                        OOTDPostResponse.getCommentCount());

                // get count from cache or add all counts to cache
                PostCountVO cachedPostCountVO =
                    postCountRedisRepository.findOrPutPostCountVO(
                        String.valueOf(OOTDPostResponse.getId()), dbPostCountVO);

                // cache count 값으로 response를 업데이트
                OOTDPostResponse.setViewCount(cachedPostCountVO.getViewCount());
                OOTDPostResponse.setLikeCount(cachedPostCountVO.getLikeCount());

                // Add the modified PostResponse to the list
                modifiedMyOOTDPostResponses.add(OOTDPostResponse);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });

    // TODO: 정렬 기준은 하나로 고정
    List<String> sortProperties = getSortProperties(pageable);
    if (sortProperties.get(0).equals("viewCount")) {
      modifiedMyOOTDPostResponses.sort(Comparator.comparing(OOTDPostResponse::getViewCount).reversed());
    } else if (sortProperties.get(0).equals("likeCount")) {
      modifiedMyOOTDPostResponses.sort(Comparator.comparing(OOTDPostResponse::getLikeCount).reversed());
    }

    OOTDPostPageResponse sortedPostPageResponse =
            OOTDPostPageResponse.fromDto(
                    new PageImpl<>(
                            modifiedMyOOTDPostResponses,
                            myOOTDPostResponses.getPageable(),
                            myOOTDPostResponses.getTotalElements()));

    return sortedPostPageResponse;
  }

  public OOTDPostPageResponse getMemberOOTDPosts(
      Long postMemberId, Long memberId, Pageable pageable) {
    Page<OOTDPostResponse> memberOOTDPostResponses =
        postRepository.findMemberPostsByMemberId(postMemberId, memberId, pageable);

    List<OOTDPostResponse> modifiedMemberOOTDPostResponses = new ArrayList<>();

    memberOOTDPostResponses
        .getContent()
        .forEach(
            OOTDPostResponse -> {
              try {
                PostCountVO dbPostCountVO =
                    new PostCountVO(
                        OOTDPostResponse.getViewCount(),
                        OOTDPostResponse.getLikeCount(),
                        OOTDPostResponse.getCommentCount());

                // get count from cache or add all counts to cache
                PostCountVO cachedPostCountVO =
                    postCountRedisRepository.findOrPutPostCountVO(
                        String.valueOf(OOTDPostResponse.getId()), dbPostCountVO);

                // cache count 값으로 response를 업데이트
                OOTDPostResponse.setViewCount(cachedPostCountVO.getViewCount());
                OOTDPostResponse.setLikeCount(cachedPostCountVO.getLikeCount());

                // Add the modified PostResponse to the list
                modifiedMemberOOTDPostResponses.add(OOTDPostResponse);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });

    // TODO: 정렬 기준은 하나로 고정
    List<String> sortProperties = getSortProperties(pageable);
    if (sortProperties.get(0).equals("viewCount")) {
      modifiedMemberOOTDPostResponses.sort(Comparator.comparing(OOTDPostResponse::getViewCount).reversed());
    } else if (sortProperties.get(0).equals("likeCount")) {
      modifiedMemberOOTDPostResponses.sort(Comparator.comparing(OOTDPostResponse::getLikeCount).reversed());
    }

    OOTDPostPageResponse sortedPostPageResponse =
            OOTDPostPageResponse.fromDto(
                    new PageImpl<>(
                            modifiedMemberOOTDPostResponses,
                            memberOOTDPostResponses.getPageable(),
                            memberOOTDPostResponses.getTotalElements()));

    return sortedPostPageResponse;
  }

  public List<Top4OOTDResponse> getTop4OOTDPosts(Long productId) {
    try {
      List<Top4OOTDVO> cachedTop4OOTDVOs =
          top4OOTDRedisRepository.findOrPutTop4OOTDVO(String.valueOf(productId));
      return cachedTop4OOTDVOs.stream()
          .map(Top4OOTDResponse::fromTop4OOTDVO)
          .collect(Collectors.toList());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  public void addViewCount(Long id) {
    Post post = postRepository.findByIdAndIsDeletedFalse(id);
    try {
      PostCountVO dbPostCountVO =
          new PostCountVO(post.getViewCount(), post.getLikeCount(), post.getCommentCount());
      PostCountVO cachedPostCountVO =
          postCountRedisRepository.findOrPutPostCountVO(String.valueOf(id), dbPostCountVO);
      cachedPostCountVO.addViewCount(1);
      // update view count to cache
      postCountRedisRepository.modifyPostCountVOAboutLikeCount(
          String.valueOf(id), cachedPostCountVO);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public PostDetailResponse findDetailByIdWithIsFollowing(Long id, Long memberId) {
    PostDetailResponse postDetailResponse =
        postRepository.findDetailByIdWithIsFollowingAndIsLike(id, memberId);
    if (postDetailResponse == null) {
      throw new PostEntityNotFoundException();
    }
    List<Long> productIds =
        postDetailResponse.getPostImageProductDetails().stream()
            .map(PostImageProductDetailResponse::getProductId)
            .collect(Collectors.toList());

    List<ProductInfoResponse> productInfos;
    if (productIds.size() == 1 && Objects.isNull(productIds.get(0))) {
      productInfos = new ArrayList<>();
      postDetailResponse.getPostImageProductDetails().clear();
    } else {
      // feign client call
      productInfos =
          Objects.requireNonNull(productServiceClient.getProductInfos(productIds).getBody())
              .getProductInfos();
    }

    List<CouponForProductResponse> couponsForProduct;
    if (Objects.nonNull(memberId)) {
      couponsForProduct =
          promotionServiceClient.getCouponsForProduct(memberId, productIds).getBody();
    } else {
      couponsForProduct = new ArrayList<>();
    }

    postDetailResponse
        .getPostImageProductDetails()
        .forEach(
            pipd -> {
              productInfos.forEach(
                  pi -> {
                    if (pipd.getProductId().equals(pi.getId())) {
                      pipd.setFromProductInfoResponse(pi);
                    }
                  });
              couponsForProduct.forEach(
                  cfp -> {
                    if (pipd.getProductId().equals(cfp.getProductId())) {
                      pipd.setHasAvailableCoupon(cfp.getHasAvailableCoupon());
                    }
                  });
            });

    try {
      PostCountVO dbPostCountVO =
          new PostCountVO(
              postDetailResponse.getViewCount(),
              postDetailResponse.getLikeCount(),
              postDetailResponse.getCommentCount());

      // get count from cache or add all counts to cache
      PostCountVO cachedPostCountVO =
          postCountRedisRepository.findOrPutPostCountVO(
              String.valueOf(postDetailResponse.getId()), dbPostCountVO);

      postDetailResponse.setViewCount(cachedPostCountVO.getViewCount());
      postDetailResponse.setLikeCount(cachedPostCountVO.getLikeCount());
      postDetailResponse.setCommentCount(cachedPostCountVO.getCommentCount());

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return postDetailResponse;
  }

  public List<String> getSortProperties(Pageable pageable) {
    Sort sort = pageable.getSort();

    List<String> sortColumnNames = new ArrayList<>();

    for (Sort.Order order : sort) {
      sortColumnNames.add(order.getProperty());
    }

    return sortColumnNames;
  }
}
