package com.xpto.resort.service.dto.quarto;

import java.math.BigDecimal;

public record QuartoResponseDto(
        Integer id,
        String nome,
        String descricao,
        Boolean vistaMar,
        Integer capacidade,
        BigDecimal valorDia,
        String status
        ) {
}
