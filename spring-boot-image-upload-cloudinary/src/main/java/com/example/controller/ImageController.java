package com.example.controller;

import com.example.dto.UploadImageResponse;
import com.example.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Note::
     * If you are trying to upload file info in JSON format, then you will get an error. Because MultipartFile cannot be
     * deserialized from JSON. Jackson (which parses @RequestBody) doesn’t know how to convert JSON → MultipartFile,
     * since files are binary, not JSON objects. I made this mistake at first. I will keep the code so that you can
     * understand the error. Sometimes it’s better to make an error 😅.
     * </p>
     *
     * ++++++ WRONG CODE: DON'T DO ❌❌❌  ++++++
     * @PostMapping
     * public ResponseEntity<UploadImageResponse> uploadImage(@RequestBody UploadImageRequest request) {
     *      return ResponseEntity.ok(imageService.uploadImage(request));
     * }
     *
     * ++++++ JSON: DON'T DO ❌❌❌ ++++++
     * {
     *     "name" : "",
     *     "file" : ""
     * }
     */
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadImageResponse> uploadImage(
            @RequestPart("name") String name, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.uploadImage(name, file));
    }
}
