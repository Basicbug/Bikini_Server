package com.basicbug.bikini.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    boolean upload(MultipartFile multipartFile);
}
