package com.basicbug.bikini.controller;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    @GetMapping("/image/dummy")
    public ResponseEntity<byte[]> getDummyImage() throws IOException {
        // getFile
        ClassPathResource resource = new ClassPathResource("static/thumbnail/dummy.png");
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(resource.getInputStream()));
    }
}
