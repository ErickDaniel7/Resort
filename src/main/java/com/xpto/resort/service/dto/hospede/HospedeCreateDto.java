package com.xpto.resort.service.dto.hospede;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HospedeCreateDto(

        @NotBlank(message = "Nome de hóspede não poder ficar vazio")
        String nome,
        @NotBlank(message = "CPF não pode ser vazio")
        String cpf,
        @NotBlank(message = "RG não pode ser vazio")
        String rg,
        @NotBlank(message = "Telefone não pode ser vazio")
        String telefone,
        @NotNull(message = "Data de Nascimento não pode ser vazio")
        LocalDate dataNascimento ) {
}
