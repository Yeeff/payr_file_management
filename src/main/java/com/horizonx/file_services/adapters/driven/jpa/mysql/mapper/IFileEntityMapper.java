package com.horizonx.file_services.adapters.driven.jpa.mysql.mapper;

import com.horizonx.file_services.adapters.driven.jpa.mysql.entity.FileEntity;
import com.horizonx.file_services.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileEntityMapper {

    FileEntity toEntity(FileModel fileModel);

    FileModel toModel(FileEntity fileEntity);

    List<FileModel> toFileModelList(List<FileEntity> fileEntitiesList);
}
