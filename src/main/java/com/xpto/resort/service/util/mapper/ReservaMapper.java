package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Reserva;
import com.xpto.resort.model.StatusReserva;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservaMapper {
    ReservaMapper instance = Mappers.getMapper(ReservaMapper.class);
    Reserva toEntity(ReservaInput reservaInput);
    ReservaResponse toDto(Reserva reserva);
    String statusAsString(StatusReserva status);
}
