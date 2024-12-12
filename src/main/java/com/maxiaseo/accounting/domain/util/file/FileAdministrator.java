package com.maxiaseo.accounting.domain.util.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileAdministrator {

    public static void overwriteTempFile(String tempFileName, byte[] data) throws IOException {

        String tempFolder = System.getProperty("java.io.tmpdir");

        File tempFile = new File(tempFolder, tempFileName);

        if (!tempFile.exists()) {
            throw new IOException("Temporary file does not exist: " + tempFile.getAbsolutePath());
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }

        System.out.println("File overwritten successfully: " + tempFile.getAbsolutePath());
    }

    public static byte[] getDataInMemoryFromTempFileByName(String tempFileName)throws IOException{
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, tempFileName);

        FileInputStream fis;

        if (!tempFile.exists()) {
            System.out.println("The temporary file: " + tempFile.getAbsolutePath() + " does not exist.");
        }

        if (tempFile != null && tempFile.exists()) {

            fis = new FileInputStream(tempFile);

        } else {
            throw new IOException("Temporary file does not exist.");
        }
        return saveInMemory(fis);
    }

    public static byte[] saveInMemory(InputStream inputStream) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        byte[] inputStreamBytes = buffer.toByteArray();

        return inputStreamBytes;
    }

    public static File saveTemporaryFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded-", file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        return tempFile;
    }

    public static File saveTemporaryFileFromInMemoryBytes(byte[] inMemoryInputStreamBytesFile) throws IOException {

        File tempFile = File.createTempFile("uploaded-", ".xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
            fileOut.write(inMemoryInputStreamBytesFile);
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
