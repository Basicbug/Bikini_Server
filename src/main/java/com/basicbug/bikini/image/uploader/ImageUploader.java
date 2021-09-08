package com.basicbug.bikini.image.uploader;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile multipartFile, String dirName);
}
