package com.xpto.resort.service.dto.quarto;

import com.xpto.resort.model.StatusQuarto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record QuartoCreateDto(

        @NotNull(message="Campo nome não pode ser nulo")
        @NotBlank(message="Campo nome não pode ser em branco")
        String nome,
        String descricao,
        Boolean vistaMar,
        @NotNull(message = "Campo capcidade não pode ser nulo")
        @Positive(message = "Campo capacidade deve ser um número maior que zero")
        Integer capacidade,
        @NotNull(message = "Campo Valor de Diária não pode ser vazio")
        BigDecimal valorDia
        ) {
}
