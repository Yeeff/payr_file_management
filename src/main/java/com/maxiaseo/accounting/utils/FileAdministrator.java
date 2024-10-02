package com.maxiaseo.accounting.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAdministrator {

    // Save the file temporarily on the disk
    public File saveTemporaryFile(MultipartFile file) throws IOException {
        // Create a unique temp file in the default temporary directory
        File tempFile = File.createTempFile("uploaded-", file.getOriginalFilename());

        // Write the uploaded file content to the temp file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;  // Return the temp file reference for later use
    }

    // Method to retrieve the processed file (if needed)
    public File getProcessedFile(File tempFile) {
        return tempFile.exists() ? tempFile : null;
    }

    // Delete the temporary file after it's no longer needed
    public void deleteTemporaryFile(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();  // Remove the file from the temporary location
        }
    }

}
