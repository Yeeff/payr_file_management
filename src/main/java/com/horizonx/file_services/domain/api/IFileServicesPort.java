package com.horizonx.file_services.domain.api;

import com.horizonx.file_services.domain.model.FileModel;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IFileServicesPort {

    FileModel saveFile(InputStream fis, Integer year, Integer month, Integer day, Integer formId) throws IOException;

    void saveSiigoFormat(InputStream fis) throws IOException;

    void deleteTemporaryFile(String fileName);

    File processSiigoFormat(String tempFileName) throws IOException;

    List<FileModel> getFiles();

    FileModel getFileContent(String tempFileName);

    byte[] downloadFileByname(String fileName) throws IOException;

    FileModel getFileContentByFormId(Integer formId);
}
