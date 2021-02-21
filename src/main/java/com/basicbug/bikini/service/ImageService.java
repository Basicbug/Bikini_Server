package com.basicbug.bikini.service;

import com.basicbug.bikini.model.FeedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUploader uploader;

    public FeedImage uploadImage(MultipartFile image, String dirName) {
        final String fileName = getFileName(image, dirName);
        final String imageUrl = uploader.upload(image, dirName);

        return FeedImage.builder()
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .name(fileName)
                        .url(imageUrl)
                        .build();
    }

    public List<FeedImage> uploadImages(List<MultipartFile> images, String dirName) {
        return images.stream()
            .map(image -> uploadImage(image, dirName))
            .collect(Collectors.toList());
    }

    private String getFileName(MultipartFile file, String dirName) {
        return String.format("%s%s%s_%s", dirName, File.separator, getUUID(), file.getName());
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
