package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.service.dto.hospede.HospedeInput;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T14:14:24-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class HospedeMapperImpl implements HospedeMapper {

    @Override
    public Hospede toEntity(HospedeInput hospedeInput) {
        if ( hospedeInput == null ) {
            return null;
        }

        Hospede hospede = new Hospede();

        hospede.setId( hospedeInput.id() );
        hospede.setNome( hospedeInput.nome() );
        hospede.setTelefone( hospedeInput.telefone() );
        hospede.setCpf( hospedeInput.cpf() );
        hospede.setRg( hospedeInput.rg() );
        hospede.setDataNascimento( hospedeInput.dataNascimento() );

        return hospede;
    }

    @Override
    public HospedeResponse toDto(Hospede hospede) {
        if ( hospede == null ) {
            return null;
        }

        Integer id = null;
        String nome = null;
        String cpf = null;
        String rg = null;
        String telefone = null;
        LocalDate dataNascimento = null;

        id = hospede.getId();
        nome = hospede.getNome();
        cpf = hospede.getCpf();
        rg = hospede.getRg();
        telefone = hospede.getTelefone();
        dataNascimento = hospede.getDataNascimento();

        HospedeResponse hospedeResponse = new HospedeResponse( id, nome, cpf, rg, telefone, dataNascimento );

        return hospedeResponse;
    }
}
