package com.xpto.resort.service.dto.hospede;

import java.time.LocalDate;

public record HospedeResponse(
        Integer id,
        String nome,
        String cpf,
        String rg,
        String telefone,
        LocalDate dataNascimento) {
}