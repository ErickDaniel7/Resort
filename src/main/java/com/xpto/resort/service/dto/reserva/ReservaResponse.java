package com.xpto.resort.service.dto.reserva;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservaResponse(
        Integer id,
        Hospede hospede,
        Quarto quarto,
        LocalDate dataEntrada,
        LocalDate dataSaida,
        String status
) {
}
