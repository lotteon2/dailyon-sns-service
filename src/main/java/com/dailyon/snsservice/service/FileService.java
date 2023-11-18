package com.dailyon.snsservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileService {

  public String generateUniqueFileName(String filename) {
    return UUID.randomUUID() + filename;
  }
}
