package com.horizonx.file_services.adapters.driving.http.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class ExcelMapper {

    public InputStream fileExcelToInputstream(MultipartFile file){

        InputStream fis = null;

        if (!file.getOriginalFilename().endsWith(".xls") && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("The specified file is not an Excel file");
        }

        try {
            fis = file.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fis;
    }
}
