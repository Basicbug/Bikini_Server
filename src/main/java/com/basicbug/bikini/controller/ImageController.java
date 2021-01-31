package com.basicbug.bikini.controller;

import com.basicbug.bikini.dto.common.CommonResponse;
import com.basicbug.bikini.service.ImageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/dummy")
    public ResponseEntity<byte[]> getDummyImage() throws IOException {
        // getFile
        ClassPathResource resource = new ClassPathResource("static/thumbnail/dummy.png");
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(resource.getInputStream()));
    }

    @PostMapping("/upload")
    public CommonResponse<Boolean> uploadImage(@RequestParam("data") MultipartFile multipartFile) {
        boolean result = imageService.uploadImage(multipartFile);
        return CommonResponse.of(result);
    }
}
