package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.service.dto.hospede.HospedeInput;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HospedeMapper {

    HospedeMapper instance = Mappers.getMapper(HospedeMapper.class);
    Hospede toEntity(HospedeInput hospedeInput);
    HospedeResponse toDto(Hospede hospede);
}
