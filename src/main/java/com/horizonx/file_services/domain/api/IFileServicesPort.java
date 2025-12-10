package com.horizonx.file_services.domain.api;

import com.horizonx.file_services.adapters.driving.http.dto.EmployeeOvertimeDto;
import com.horizonx.file_services.domain.model.FileModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IFileServicesPort {

    FileModel saveFile(InputStream fis, Integer year, Integer month, Integer day) throws IOException;

    void saveSiigoFormat(InputStream fis) throws IOException;

    void deleteTemporaryFile(String fileName);

    File processSiigoFormat(String tempFileName) throws IOException;

    List<FileModel> getFiles();

    FileModel getFileContent(String tempFileName);

    byte[] downloadFileByname(String fileName) throws IOException;

    String createEmployeeOvertimeReport(List<EmployeeOvertimeDto> employees) throws IOException;

}
