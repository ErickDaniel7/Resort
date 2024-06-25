package com.xpto.resort.repository;

import com.xpto.resort.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuartoRepository extends QuartoRepositoryCustom , JpaRepository<Quarto,Integer>    {
    @Query("SELECT q FROM Quarto q WHERE q.id NOT IN (" +
            "SELECT r.quarto.id FROM Reserva r " +
            "WHERE (r.dataEntrada < :dataSaida AND r.dataSaida > :dataEntrada) " +
            "AND (r.status = 'ABERTO' OR r.status = 'PENDENTE'))")
    List<Quarto> findAvailableRooms(@Param("dataEntrada") LocalDate dataEntrada,
                                    @Param("dataSaida") LocalDate dataSaida);

    @Query("SELECT q FROM Quarto q LEFT JOIN Reserva r ON q.id = r.quarto.id AND " +
            "(r.dataEntrada <= :dataSaida AND r.dataSaida >= :dataEntrada AND (r.status = 'ABERTO' OR r.status = 'PENDENTE')) " +
            "WHERE (:vistaMar IS NULL OR q.vistaMar = :vistaMar) " +
            "AND (:quantidadeQuartos IS NULL OR q.capacidade = :quantidadeQuartos) " +
            "AND (r.id IS NULL OR r.dataEntrada > :dataSaida OR r.dataSaida < :dataEntrada)")
    List<Quarto> findRooms(
            @Param("dataEntrada") LocalDate dataEntrada,
            @Param("dataSaida") LocalDate dataSaida,
            @Param("vistaMar") Boolean vistaMar,
            @Param("quantidadeQuartos") Integer quantidadeQuartos);
}
