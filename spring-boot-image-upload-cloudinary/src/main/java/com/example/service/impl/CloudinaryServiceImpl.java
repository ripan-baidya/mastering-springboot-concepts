package com.example.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folderName,
                    "resource_type", "auto" // automatically detect the file type
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadedFile.get("public_id");
            String url = cloudinary.url().secure(true).generate(publicId);

            log.debug("Successfully uploaded file to URL: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Failed to upload file");
            throw new RuntimeException("File Uploaded failed!");
        }
    }
}
