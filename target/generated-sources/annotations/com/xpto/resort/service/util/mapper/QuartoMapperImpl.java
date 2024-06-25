package com.xpto.resort.service.util.mapper;

import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.StatusQuarto;
import com.xpto.resort.service.dto.quarto.QuartoResponseDto;
import com.xpto.resort.service.dto.quarto.QuartoUpdateDto;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T14:14:24-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class QuartoMapperImpl implements QuartoMapper {

    @Override
    public Quarto toEntity(QuartoUpdateDto quartoUpdateDto) {
        if ( quartoUpdateDto == null ) {
            return null;
        }

        Quarto quarto = new Quarto();

        quarto.setId( quartoUpdateDto.id() );
        quarto.setNome( quartoUpdateDto.nome() );
        quarto.setVistaMar( quartoUpdateDto.vistaMar() );
        quarto.setValorDia( quartoUpdateDto.valorDia() );
        quarto.setCapacidade( quartoUpdateDto.capacidade() );
        quarto.setStatus( quartoUpdateDto.status() );
        quarto.setDescricao( quartoUpdateDto.descricao() );

        return quarto;
    }

    @Override
    public QuartoResponseDto toDto(Quarto quarto) {
        if ( quarto == null ) {
            return null;
        }

        Integer id = null;
        String nome = null;
        String descricao = null;
        Boolean vistaMar = null;
        Integer capacidade = null;
        BigDecimal valorDia = null;
        String status = null;

        id = quarto.getId();
        nome = quarto.getNome();
        descricao = quarto.getDescricao();
        vistaMar = quarto.getVistaMar();
        capacidade = quarto.getCapacidade();
        valorDia = quarto.getValorDia();
        status = statusAsString( quarto.getStatus() );

        QuartoResponseDto quartoResponseDto = new QuartoResponseDto( id, nome, descricao, vistaMar, capacidade, valorDia, status );

        return quartoResponseDto;
    }

    @Override
    public String statusAsString(StatusQuarto status) {
        if ( status == null ) {
            return null;
        }

        String string;

        switch ( status ) {
            case DISPONIVEL: string = "DISPONIVEL";
            break;
            case OCUPADO: string = "OCUPADO";
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        return string;
    }
}
