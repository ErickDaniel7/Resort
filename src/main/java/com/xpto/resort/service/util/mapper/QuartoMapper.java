package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.StatusQuarto;
import com.xpto.resort.service.dto.quarto.QuartoResponseDto;
import com.xpto.resort.service.dto.quarto.QuartoUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuartoMapper {
    QuartoMapper Instance = Mappers.getMapper(QuartoMapper.class);
    Quarto toEntity(QuartoUpdateDto quartoUpdateDto);
    //quarto toEntity(QuartoCreateDto quartoCreateDto);


   
    QuartoResponseDto toDto(Quarto quarto);

    String statusAsString(StatusQuarto status);
}
