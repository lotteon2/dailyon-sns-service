package com.dailyon.snsservice.mapper.hashtag;

import com.dailyon.snsservice.entity.HashTag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashTagMapper {

  public List<HashTag> createHashTags(List<String> hashTagNames) {
    return hashTagNames.stream().map(HashTag::createHashTag).collect(Collectors.toList());
  }
}
