package com.maxiaseo.accounting.adapters.driving.http.mapper;

import com.maxiaseo.accounting.adapters.driving.http.dto.FileResponseDto;
import com.maxiaseo.accounting.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileResponseMapper {

    List<FileResponseDto> toListDto(List<FileModel> fileModel);

    FileResponseDto toFileDto (FileModel fileModel);
}
