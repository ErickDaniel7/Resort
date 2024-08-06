package com.xpto.resort.service.dto.hospede;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HospedeUpdateDto(
        Integer id,
        @NotBlank(message="Campo Nome de Hospede não pode ser vazio")
        String nome,
        @NotBlank(message="Campo CPF não pode ser vazio")
        String cpf,
        @NotBlank(message="Campo RG não pode ser vazio")
        String rg,
        @NotBlank(message="Camplo Telefone não pode ser vazio")
        String telefone,
        @NotNull(message="Campo Data de Nascimento não pode ser vazio")
        LocalDate dataNascimento) {
}
