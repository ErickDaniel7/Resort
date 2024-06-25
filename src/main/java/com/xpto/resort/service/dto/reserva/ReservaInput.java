package com.xpto.resort.service.dto.reserva;

import java.time.LocalDate;

public record ReservaInput(Integer id, Integer idHospede, Integer idQuarto, LocalDate dataEntrada, LocalDate dataSaida) {
}
