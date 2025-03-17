package com.maxiaseo.accounting.adapters.driven.feign.mapper;

import com.maxiaseo.accounting.adapters.driven.feign.dto.FileClientRequestDto;
import com.maxiaseo.accounting.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileClientRequestMapper {

    List<FileClientRequestDto> toListDto(List<FileModel> fileModel);

    FileClientRequestDto toFileDto (FileModel fileModel);
}
