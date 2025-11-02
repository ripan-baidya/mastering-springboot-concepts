package com.example.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    /**
     * Uploads a file to Cloudinary and returns the URL of the uploaded file.
     * The folder name is constructed using the following strategy: <application>/<entity>/<id>
     * For example, if the application is "app", the entity is "user_image", and the id is 12,
     * the folder name will be "app/user_image/12".
     * @param file The file to be uploaded.
     * @param folderName The name of the folder where the file will be stored.
     * @return The URL of the uploaded file.
     */
    String uploadFile(MultipartFile file, String folderName);
}
