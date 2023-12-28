package com.dailyon.snsservice.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Top4OOTDVO implements Serializable {

  private Long id;
  private String thumbnailImgUrl;
}
