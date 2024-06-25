package com.xpto.resort.repository;

import com.xpto.resort.model.Quarto;
import java.util.List;


public interface QuartoRepositoryCustom {
    List<Quarto> findQuartosDinamico(Boolean vistaMar, Integer quantidadeQuartos);
}
