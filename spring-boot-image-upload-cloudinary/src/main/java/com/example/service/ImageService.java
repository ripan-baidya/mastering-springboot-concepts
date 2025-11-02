package com.example.service;

import com.example.dto.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    UploadImageResponse uploadImage(String name, MultipartFile file);
}
