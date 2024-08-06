package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Reserva;
import com.xpto.resort.model.StatusReserva;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-29T19:09:26-0300",
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
        LocalDate dataEntrada = null;
        LocalDate dataSaida = null;
        String status = null;
        BigDecimal valorTotal = null;

        id = reserva.getId();
        dataEntrada = reserva.getDataEntrada();
        dataSaida = reserva.getDataSaida();
        status = statusAsString( reserva.getStatus() );
        valorTotal = reserva.getValorTotal();

        Integer idQuarto = null;
        String nomeQuarto = null;
        Integer idHospede = null;
        String nomeHospede = null;
        String cpfHospede = null;

        ReservaResponse reservaResponse = new ReservaResponse( id, idQuarto, nomeQuarto, idHospede, nomeHospede, cpfHospede, dataEntrada, dataSaida, status, valorTotal );

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
