package com.dailyon.snsservice.kafka;

import com.dailyon.snsservice.service.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailyon.domain.common.KafkaTopic;
import dailyon.domain.sns.kafka.dto.MemberCreateDTO;
import dailyon.domain.sns.kafka.dto.MemberUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

  private final MemberService memberService;
  private final ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(topics = KafkaTopic.CREATE_MEMBER_FOR_SNS)
  public void createMember(String message, Acknowledgment ack) {
    MemberCreateDTO memberCreateDTO;
    try {
      memberCreateDTO = objectMapper.readValue(message, MemberCreateDTO.class);
      memberService.createMember(memberCreateDTO);

      ack.acknowledge();
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Transactional
  @KafkaListener(topics = KafkaTopic.UPDATE_MEMBER_FOR_SNS)
  public void updateMember(String message, Acknowledgment ack) {
    MemberUpdateDTO memberUpdateDTO;
    try {
      memberUpdateDTO = objectMapper.readValue(message, MemberUpdateDTO.class);
      memberService.updateMember(memberUpdateDTO);

      ack.acknowledge();
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
