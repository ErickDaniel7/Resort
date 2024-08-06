package com.xpto.resort.service.dto.reserva;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.Reserva;
import com.xpto.resort.model.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservaResponse(
        Integer id,
        Integer idQuarto,
        String nomeQuarto,
        Integer idHospede,
        String nomeHospede,
        String cpfHospede,
        LocalDate dataEntrada,
        LocalDate dataSaida,
        String status,
        BigDecimal valorTotal
) {

    public ReservaResponse(Reserva reserva) {
        this(
                reserva.getId(),
                reserva.getQuarto().getId(),
                reserva.getQuarto().getNome(),
                reserva.getHospede().getId(),
                reserva.getHospede().getNome(),
                reserva.getHospede().getCpf(),
                reserva.getDataEntrada(),
                reserva.getDataSaida(),
                reserva.getStatus().toString(),
                reserva.getValorTotal()
        );
    }
}
