package com.xpto.resort.service.dto.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservaInput(
        Integer id,
        @NotNull(message="É necessário informar o Hóspede")
        Integer idHospede,
        @NotNull(message="É necessário informar o Quarto")
        Integer idQuarto,
        @NotNull(message="É necessário informar a Data de Entrada")
        LocalDate dataEntrada,
        @NotNull(message = "É necessário informar a Data de Saída")
        LocalDate dataSaida) {
}
