package com.basicbug.bikini.service;

import com.basicbug.bikini.model.FeedImage;
import com.basicbug.bikini.repository.FeedImageRepository;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUploader uploader;
    private final FeedImageRepository feedImageRepository;

    @Transactional
    public FeedImage uploadImage(MultipartFile image, String dirName) {
        final String fileName = getFileName(image, dirName);
        final String imageUrl = uploader.upload(image, dirName);
        final FeedImage feedImage = FeedImage.builder()
                                            .createdAt(LocalDateTime.now())
                                            .modifiedAt(LocalDateTime.now())
                                            .name(fileName)
                                            .url(imageUrl)
                                            .build();

        feedImageRepository.save(feedImage);
        return feedImage;
    }

    public List<FeedImage> uploadImages(List<MultipartFile> images, String dirName) {
        return images.stream()
            .map(image -> uploadImage(image, dirName))
            .collect(Collectors.toList());
    }

    public List<FeedImage> findAllFeedImageByIds(List<Long> feedIds) {
        if (Objects.isNull(feedIds)) {
            return Collections.emptyList();
        }

        return feedImageRepository.findAllById(feedIds);
    }

    private String getFileName(MultipartFile file, String dirName) {
        return String.format("%s%s%s_%s", dirName, File.separator, getUUID(), file.getName());
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
