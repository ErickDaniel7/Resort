package com.xpto.resort.service.dto.quarto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuartoInput {
    private Integer id;
    private String nome;
    private BigDecimal valorDia;
    private Boolean vistaMar;
    private String descricao;
    private Integer capacidade;

    public String toString(){
        return (this.getId()+","+this.getNome()+","+this.getDescricao());
    }
}
