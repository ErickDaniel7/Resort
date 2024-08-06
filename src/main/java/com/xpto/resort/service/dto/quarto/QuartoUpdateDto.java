package com.xpto.resort.service.dto.quarto;

import com.xpto.resort.model.StatusQuarto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record QuartoUpdateDto(
        @NotNull(message="Id de quarto não foi repassado")
        Integer id,

        @NotNull(message = "Nome não pode ser nulo")
        @NotBlank(message = "Nome não pode ser em branco")
        String nome,

        Boolean vistaMar,

        @NotNull(message = "Quantidade de quartos não pode ser nulo")
        @Positive(message="Capacidade do quarto deve ser um número positivo")
        Integer capacidade,

        @NotNull(message = "Valor da Diária de quartos não pode ser nulo")
        BigDecimal valorDia,

        String descricao,

        StatusQuarto status  ) {
}
