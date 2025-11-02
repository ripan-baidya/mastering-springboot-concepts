package com.example.service.impl;

import com.example.dto.UploadImageResponse;
import com.example.entity.Image;
import com.example.repository.ImageRepository;
import com.example.service.CloudinaryService;
import com.example.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(CloudinaryService cloudinaryService, ImageRepository imageRepository) {
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override
    public UploadImageResponse uploadImage(String name, MultipartFile file) {
        // Validation
        if (file == null || file.isEmpty()) {
            return new UploadImageResponse(false, "file is empty", null);
        }
        if (name == null || name.isEmpty()) {
            return new UploadImageResponse(false, "image name is required", null);
        }

        // Upload the image to cloudinary
        String imageUrl = cloudinaryService.uploadFile(file, "images");
        if (imageUrl == null) {
            return new UploadImageResponse(false, "Image not uploaded!, try again", null);
        }

        Image newImage = new Image();
        newImage.setImageName(name);
        newImage.setImageUrl(imageUrl);

        imageRepository.save(newImage);
        log.info("Image [{}] saved successfully at {}", newImage.getImageName(), newImage.getImageUrl());

        return new UploadImageResponse(true, "Image upload successfully!", imageUrl);
    }
}
