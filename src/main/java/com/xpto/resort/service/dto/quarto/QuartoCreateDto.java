package com.xpto.resort.service.dto.quarto;

import com.xpto.resort.model.StatusQuarto;

import java.math.BigDecimal;

public record QuartoCreateDto(String nome, Boolean vistaMar, Integer capacidade, BigDecimal valorDia, StatusQuarto status) {
}
