package com.maxiaseo.accounting.domain.util.file;

import java.io.*;

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

    public static byte[] saveInMemory(InputStream inputStream) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }


    public static File saveTemporaryFileFromInMemoryBytes(byte[] inMemoryInputStreamBytesFile) throws IOException {

        File tempFile = File.createTempFile("uploaded-", ".xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
            fileOut.write(inMemoryInputStreamBytesFile);
        }

        return tempFile;
    }



}
