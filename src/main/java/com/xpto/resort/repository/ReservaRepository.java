package com.xpto.resort.repository;

import com.xpto.resort.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Integer> {
    List<Reserva> findByHospedeNomeContaining(String nome);
    List<Reserva> findByDataEntrada(LocalDate dataInicio);

    @Query("SELECT r FROM Reserva r " +
            "WHERE r.quarto.id = :quartoId " +
            "AND r.status != 'FECHADO' " +
            "AND ((r.dataEntrada BETWEEN :dataEntrada AND :dataSaida) " +
            "    OR (r.dataSaida BETWEEN :dataEntrada AND :dataSaida))"
    )
    List<Reserva> findConflitingReservas(Integer quartoId, LocalDate dataEntrada, LocalDate dataSaida);
}
