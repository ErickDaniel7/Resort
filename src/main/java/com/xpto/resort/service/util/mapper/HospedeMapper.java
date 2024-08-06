package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.service.dto.hospede.HospedeCreateDto;
import com.xpto.resort.service.dto.hospede.HospedeUpdateDto;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HospedeMapper {

    HospedeMapper instance = Mappers.getMapper(HospedeMapper.class);
    Hospede toUpdateEntity(HospedeUpdateDto hospedeUpdateDto);

    Hospede toCreateEntity(HospedeCreateDto hospedeCreateDto);
    HospedeResponse toDto(Hospede hospede);
}
