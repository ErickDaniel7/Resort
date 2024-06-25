package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.Reserva;
import com.xpto.resort.model.StatusReserva;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T17:12:45-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class ReservaMapperImpl implements ReservaMapper {

    @Override
    public Reserva toEntity(ReservaInput reservaInput) {
        if ( reservaInput == null ) {
            return null;
        }

        Reserva reserva = new Reserva();

        reserva.setId( reservaInput.id() );
        reserva.setDataEntrada( reservaInput.dataEntrada() );
        reserva.setDataSaida( reservaInput.dataSaida() );

        return reserva;
    }

    @Override
    public ReservaResponse toDto(Reserva reserva) {
        if ( reserva == null ) {
            return null;
        }

        Integer id = null;
        Hospede hospede = null;
        Quarto quarto = null;
        LocalDate dataEntrada = null;
        LocalDate dataSaida = null;
        String status = null;

        id = reserva.getId();
        hospede = reserva.getHospede();
        quarto = reserva.getQuarto();
        dataEntrada = reserva.getDataEntrada();
        dataSaida = reserva.getDataSaida();
        status = statusAsString( reserva.getStatus() );

        ReservaResponse reservaResponse = new ReservaResponse( id, hospede, quarto, dataEntrada, dataSaida, status );

        return reservaResponse;
    }

    @Override
    public String statusAsString(StatusReserva status) {
        if ( status == null ) {
            return null;
        }

        String string;

        switch ( status ) {
            case PENDENTE: string = "PENDENTE";
            break;
            case ABERTO: string = "ABERTO";
            break;
            case FECHADO: string = "FECHADO";
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        return string;
    }
}
