package com.horizonx.file_services.adapters.driven.feign.mapper;

import com.horizonx.file_services.adapters.driven.feign.dto.FileClientRequestDto;
import com.horizonx.file_services.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileClientRequestMapper {

    List<FileClientRequestDto> toListDto(List<FileModel> fileModel);

    FileClientRequestDto toFileDto (FileModel fileModel);
}
