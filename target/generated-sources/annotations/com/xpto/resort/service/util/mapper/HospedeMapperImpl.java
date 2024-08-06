package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Hospede;
import com.xpto.resort.service.dto.hospede.HospedeCreateDto;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import com.xpto.resort.service.dto.hospede.HospedeUpdateDto;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-29T19:09:26-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class HospedeMapperImpl implements HospedeMapper {

    @Override
    public Hospede toUpdateEntity(HospedeUpdateDto hospedeUpdateDto) {
        if ( hospedeUpdateDto == null ) {
            return null;
        }

        Hospede hospede = new Hospede();

        hospede.setId( hospedeUpdateDto.id() );
        hospede.setNome( hospedeUpdateDto.nome() );
        hospede.setTelefone( hospedeUpdateDto.telefone() );
        hospede.setCpf( hospedeUpdateDto.cpf() );
        hospede.setRg( hospedeUpdateDto.rg() );
        hospede.setDataNascimento( hospedeUpdateDto.dataNascimento() );

        return hospede;
    }

    @Override
    public Hospede toCreateEntity(HospedeCreateDto hospedeCreateDto) {
        if ( hospedeCreateDto == null ) {
            return null;
        }

        Hospede hospede = new Hospede();

        hospede.setNome( hospedeCreateDto.nome() );
        hospede.setTelefone( hospedeCreateDto.telefone() );
        hospede.setCpf( hospedeCreateDto.cpf() );
        hospede.setRg( hospedeCreateDto.rg() );
        hospede.setDataNascimento( hospedeCreateDto.dataNascimento() );

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
