package com.basicbug.bikini.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private ImageUploader uploader;

    public boolean uploadImage(MultipartFile multipartFile) {
        return uploader.upload(multipartFile);
    }
}
