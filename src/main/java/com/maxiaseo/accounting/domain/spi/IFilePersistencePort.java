package com.maxiaseo.accounting.domain.spi;

import com.maxiaseo.accounting.domain.model.FileModel;

import java.util.List;


public interface IFilePersistencePort {

    void addFile(FileModel fileModel);

    List<FileModel> getFiles();

    void deleteFile(String tempFileName);

    FileModel getFileByName(String name);
}
