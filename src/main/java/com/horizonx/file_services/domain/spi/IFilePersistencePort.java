package com.horizonx.file_services.domain.spi;

import com.horizonx.file_services.domain.model.FileModel;

import java.util.List;


public interface IFilePersistencePort {

    void addFile(FileModel fileModel);

    List<FileModel> getFiles();

    void deleteFile(String tempFileName);

    FileModel getFileByName(String name);
}
