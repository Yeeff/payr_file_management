package com.maxiaseo.accounting.adapters.driven.jpa.mysql.mapper;

import com.maxiaseo.accounting.adapters.driven.jpa.mysql.entity.FileEntity;
import com.maxiaseo.accounting.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileEntityMapper {

    FileEntity toEntity(FileModel fileModel);

    List<FileModel> toFileModelList(List<FileEntity> fileEntitiesList);
}
