package com.basicbug.bikini.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile multipartFile, String dirName);
}
