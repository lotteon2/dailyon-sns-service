package com.dailyon.snsservice.service.post;

import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.dto.response.post.*;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.mapper.hashtag.HashTagMapper;
import com.dailyon.snsservice.mapper.post.PostMapper;
import com.dailyon.snsservice.mapper.postimage.PostImageMapper;
import com.dailyon.snsservice.mapper.postimageproductdetail.PostImageProductDetailMapper;
import com.dailyon.snsservice.repository.post.PostRedisRepository;
import com.dailyon.snsservice.repository.post.PostRepository;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.s3.S3Service;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

  private static final String STATIC_IMG_BUCKET = "dailyon-static-dev";
  private static final String POST_STATIC_IMG_BUCKET_PREFIX = "post-img";

  private final PostRepository postRepository;
  private final PostRedisRepository postRedisRepository;
  private final PostImageProductDetailMapper postImageProductDetailMapper;
  private final PostMapper postMapper;
  private final PostImageMapper postImageMapper;
  private final HashTagMapper hashTagMapper;
  private final MemberReader memberReader;
  private final S3Service s3Service;

  public PostPageResponse getPosts(Long memberId, Pageable pageable) {
    Page<PostResponse> postResponses = postRepository.findAllWithIsLike(memberId, pageable);
    postResponses
        .getContent()
        .forEach(
            postResponse -> {
              try {
                // get count from cache
                PostCountVO DBPostCountVO =
                    new PostCountVO(
                        postResponse.getViewCount(),
                        postResponse.getLikeCount(),
                        postResponse.getCommentCount());
                PostCountVO cachedPostCountVO =
                    postRedisRepository.findOrPutPostCountVO(
                        String.valueOf(postResponse.getId()), DBPostCountVO);

                if (Objects.nonNull(cachedPostCountVO)) {
                  postResponse.setViewCount(cachedPostCountVO.getViewCount());
                  postResponse.setLikeCount(cachedPostCountVO.getLikeCount());
                }
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });
    return PostPageResponse.fromDto(postResponses);
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

    postRepository.save(post);

    String thumbnailImgPreSignedUrl = s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, thumbnailImgUrl);
    String imgPreSignedUrl = s3Service.getPreSignedUrl(STATIC_IMG_BUCKET, imgUrl);

    return CreatePostResponse.builder()
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
        .thumbnailImgPreSignedUrl(thumbnailImgPreSignedUrl)
        .imgPreSignedUrl(imgPreSignedUrl)
        .build();
  }

  @Transactional
  public void softDeletePost(Long id, Long memberId) {
    postRepository.softDeleteById(id, memberId);
    postRedisRepository.deletePostCountVO(String.valueOf(id));
  }

  public PostLikePageResponse getPostLikes(Long memberId, Pageable pageable) {
    Page<Post> posts = postRepository.findAllWithPostLikeByMemberIdIn(memberId, pageable);
    return PostLikePageResponse.fromEntity(posts);
  }

  public OOTDPostPageResponse getOOTDPosts(Long memberId, Pageable pageable) {
    Page<Post> posts = postRepository.findAllByMemberId(memberId, pageable);
    return OOTDPostPageResponse.fromEntity(posts);
  }

  public List<Top4OOTDResponse> getTop4OOTDPosts(Long productId) {
    List<Post> posts = postRepository.findTop4ByOrderByLikeCountDesc(productId);
    return posts.stream().map(Top4OOTDResponse::fromEntity).collect(Collectors.toList());
  }
}
