package com.horizonx.file_services.adapters.driving.http.mapper;

import com.horizonx.file_services.adapters.driving.http.dto.FileResponseDto;
import com.horizonx.file_services.domain.model.FileModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IFileResponseMapper {

    List<FileResponseDto> toListDto(List<FileModel> fileModel);

    FileResponseDto toFileDto (FileModel fileModel);
}
