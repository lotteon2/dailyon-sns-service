package com.dailyon.snsservice.entity.ids;

import com.dailyon.snsservice.entity.Member;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class FollowId implements Serializable {

  private Long follower;
  private Long following;
}
