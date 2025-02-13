package com.maxiaseo.accounting.adapters.driven.jpa.mysql.adapter;

import com.maxiaseo.accounting.adapters.driven.jpa.mysql.entity.FileEntity;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.mapper.IFileEntityMapper;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.repository.FileRepository;
import com.maxiaseo.accounting.domain.model.FileModel;
import com.maxiaseo.accounting.domain.spi.IFilePersistencePort;
import com.maxiaseo.accounting.adapters.driven.jpa.mysql.exception.ElementNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class FilePersistenceAdapterImpl implements IFilePersistencePort {

    private FileRepository fileRepo;
    private IFileEntityMapper fileMapper;

    public void addFile(FileModel fileModel){
        FileEntity fileEntity = fileMapper.toEntity(fileModel);
        fileRepo.save(fileEntity);
    }

    @Override
    public List<FileModel> getFiles() {
        return fileMapper.toFileModelList( fileRepo.findAll());
    }

    @Transactional
    public void deleteFile(String fileName) {
        fileRepo.deleteByName(fileName);
    }

    @Override
    public FileModel getFileByName(String name) {
        FileEntity fileEntity = fileRepo.findByName(name).orElseThrow(ElementNotFoundException::new);
        return fileMapper.toModel(fileEntity);
    }


}
