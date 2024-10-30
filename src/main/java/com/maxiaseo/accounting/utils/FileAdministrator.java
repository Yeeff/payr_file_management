package com.maxiaseo.accounting.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAdministrator {

    public static File saveTemporaryFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded-", file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

    public static Workbook getworkbook(File tempFile) throws IOException {
        Workbook workbook;
        if (tempFile != null && tempFile.exists()) {
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                workbook = WorkbookFactory.create(fis);
            } catch (IOException e) {
                throw new IOException("Failed to create a Workbook from the file", e);
            }
        } else {
            throw new IOException("Temporary file does not exist.");
        }
        return workbook;
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
