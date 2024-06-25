package com.xpto.resort.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "quarto")
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, name="vista_mar")
    private Boolean vistaMar;

    @Column(nullable = false, name="valor_dia")
    private BigDecimal valorDia;

    @Column(nullable = false, name = "qtd_max_ocupantes")
    private Integer capacidade;

    @Column(name="status")
    private StatusQuarto status = StatusQuarto.DISPONIVEL;
    // Getters and setters omitted for brevity

    @Column(name="descricao")
    private String descricao;

    @Override
    public String toString() {
        return "quarto [id=" + id + ", nome='" + nome + '\'' +
                ", vistaMar=" + vistaMar + ", valorDia=" + valorDia +
                ", capacidade=" + capacidade + ", status=" + status +
                ", descricao='" + descricao + '\'' + "]";
    }
}
