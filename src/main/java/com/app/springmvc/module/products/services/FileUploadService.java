package com.app.springmvc.module.products.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // Generate a unique filename
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename;

        // Create the upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file
        Path path = Paths.get(uploadDir + File.separator + fileName);
        Files.write(path, file.getBytes());

        return fileName;
    }
}
