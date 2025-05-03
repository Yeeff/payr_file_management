package com.horizonx.file_services.domain.service.file;

import com.horizonx.file_services.domain.util.ConstantsDomain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileAdministrator {

    private FileAdministrator(){}

    public static void overwriteTempFile(String tempFileName, byte[] data) throws IOException {

        String tempFolder = System.getProperty("java.io.tmpdir");

        File tempFile = new File(tempFolder, tempFileName);

        if (!tempFile.exists()) {
            throw new IOException("Temporary file does not exist: " + tempFile.getAbsolutePath());
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }

    }

    public static byte[] getDataInMemoryFromTempFileByName(String tempFileName)throws IOException{
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, tempFileName);

        FileInputStream fis;

        fis = new FileInputStream(tempFile);

        return saveInMemory(fis);
    }

    public static void deleteTempFileByName(String tempFileName){
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, tempFileName);
        tempFile.delete();

    }

    public static byte[] getSiigoFormat()throws IOException{
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, ConstantsDomain.SIIGO_FORMAT_NAME);

        FileInputStream fis;

        fis = new FileInputStream(tempFile);

        return saveInMemory(fis);
    }

    public static void saveSiigoFormat(byte[] content)throws IOException{

        String directoryPath = System.getProperty("java.io.tmpdir"); // System temp directory
        Path filePath = Paths.get(directoryPath, ConstantsDomain.SIIGO_FORMAT_NAME);

        try {
            // Ensure the parent directory exists
            Files.createDirectories(filePath.getParent());

            // Write the byte[] content to the file
            Files.write(filePath, content);
            System.out.println("File created and written to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    public static byte[] saveInMemory(InputStream inputStream) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }


    public static File saveTemporaryFileFromInMemoryBytes(byte[] inMemoryInputStreamBytesFile, String prefix) throws IOException {

        File tempFile = File.createTempFile( prefix, ".xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
            fileOut.write(inMemoryInputStreamBytesFile);
        }

        return tempFile;
    }



}
