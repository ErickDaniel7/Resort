package com.xpto.resort.service;

import com.xpto.resort.exceptions.RegraDeNegocioException;
import com.xpto.resort.exceptions.ResourceNotFoundException;
import com.xpto.resort.model.Hospede;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.Reserva;
import com.xpto.resort.model.StatusReserva;
import com.xpto.resort.repository.HospedeRepository;
import com.xpto.resort.repository.QuartoRepository;
import com.xpto.resort.repository.ReservaRepository;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import com.xpto.resort.service.util.mapper.ReservaMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private ReservaRepository reservaRepository;
    private HospedeRepository hospedeRepository;
    private QuartoRepository quartoRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, HospedeRepository hospedeRepository, QuartoRepository quartoRepository) {
        this.reservaRepository = reservaRepository;
        this.hospedeRepository = hospedeRepository;
        this.quartoRepository = quartoRepository;
    }

    public List<ReservaResponse> findAll() {

        List<Reserva> reservas = reservaRepository.findAll();

        return reservas
                .stream()
                .map(e -> new ReservaResponse(e))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservaResponse save(ReservaInput reservaInput) {
        Optional<Quarto> quartoOptional = quartoRepository.findById(reservaInput.idQuarto());
        Optional<Hospede> hospedeOptional = hospedeRepository.findById(reservaInput.idHospede());

        if (quartoOptional.isEmpty() || hospedeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Quarto ou Hospede não encontrado!");
        }
        validarIntervaloReserva(reservaInput.dataEntrada(),reservaInput.dataSaida());
        Quarto quarto = quartoOptional.get();
        Hospede hospede = hospedeOptional.get();

        // Verificar se há conflitos de reserva para o mesmo quarto no mesmo período
        List<Reserva> reservasConflitantes = reservaRepository.findConflitingReservas(quarto.getId(), reservaInput.dataEntrada(), reservaInput.dataSaida());

        if (reservasConflitantes.isEmpty()) {
            //throw new RuntimeException("Não é possível salvar a reserva. Já existe uma reserva para o mesmo quarto no período especificado.");
            Reserva reserva = ReservaMapper.instance.toEntity(reservaInput);
            reserva.setHospede(hospede);
            reserva.setQuarto(quarto);
            reserva.setStatus(StatusReserva.PENDENTE);
            reserva.setValorTotal(calcularValorTotal(quarto, reserva));

            if (reservaInput.id() != null) {
                Optional<Reserva> optional = reservaRepository.findById(reservaInput.id());
                if (optional.isEmpty()) {
                    throw new ResourceNotFoundException("Reserva não encontrada!");
                }
            }
            return new ReservaResponse(reservaRepository.save(reserva));
        }
        throw new RegraDeNegocioException("Não é possível salvar a reserva. Já existe uma reserva para o mesmo quarto no período especificado.");
    }

    private void validarIntervaloReserva(LocalDate dataEntrada, LocalDate dataSaida) {
        long diasDeReserva = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
        if (diasDeReserva < 2) {
            throw new RegraDeNegocioException("A reserva deve ter pelo menos 2 dias de duração.");
        }
    }

    public ReservaResponse findById(Integer id){
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);
        if (optionalReserva.isEmpty()){
            throw new ResourceNotFoundException("Reserva não encontrada!");
        }
        Reserva reserva = optionalReserva.get();
        return new ReservaResponse(reserva);

    }

    /* Por motivo de simplificação e demonstração não vou travar a alteracao caso o usuario tente fazer chekin e checkout fora da data*/
    @Transactional
    public void checkin(Integer id) {
        if (id!=null) {
            Optional<Reserva> reservaOptional = reservaRepository.findById(id);
            if (reservaOptional.isEmpty()) {
                throw new ResourceNotFoundException("Reserva não encontrada!");
            }
            Reserva reserva = reservaOptional.get();
            if (reserva.getStatus() == StatusReserva.PENDENTE) {
                reserva.setStatus(StatusReserva.ABERTO);
                reservaRepository.save(reserva); // Salva a alteração no estado da reserva
            } else {
                throw new RegraDeNegocioException("Status de reserva " + reserva.getStatus().toString() + " não pode passar para aberto");
            }
        }else {
            throw new RegraDeNegocioException("ID de reserva é necessário");
        }
    }

    /* Por motivo de simplificação e demonstração não vou travar a alteracao caso o usuario tente fazer chekin e checkout fora da data*/
    @Transactional
    public void checkout(Integer idReserva) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(idReserva);
        if (reservaOptional.isEmpty()) throw new ResourceNotFoundException("Reserva não encontrada!");
        Reserva reserva = reservaOptional.get();
        if (reserva.getId().equals(idReserva) &&  reserva.getStatus().toString() == "ABERTO") {
            reserva.setStatus(StatusReserva.FECHADO);
            //reservaRepository.save(reserva);
        } else {
            throw new RegraDeNegocioException("Status de reserva " + reserva.getStatus().toString() + " não pode passar para fechado");
        }
    }

    private BigDecimal calcularValorTotal(Quarto quarto, Reserva reserva) {
        Period period = Period.between(reserva.getDataEntrada(), reserva.getDataSaida());
        int daysBetween = period.getDays();
        return quarto.getValorDia().multiply(BigDecimal.valueOf(daysBetween));
    }

    public List<ReservaResponse> filtrarReservaPorId(Integer id) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        List<ReservaResponse> reservasFiltradas = new ArrayList<>();
        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();
            Hospede hospede = reserva.getHospede();
            Quarto quarto = reserva.getQuarto();
            reserva.setQuarto(quarto);
            reserva.setHospede(hospede);
            ReservaResponse responseRecord =  new ReservaResponse(reserva);
            reservasFiltradas.add(responseRecord);
        }
        return reservasFiltradas;
    }

    public List<ReservaResponse> filtrarReservaPorHospede(String hospede) {
        List<Reserva> reservas = reservaRepository.findByHospedeNomeContaining(hospede);
        List<ReservaResponse> reservasFiltradas = reservas.stream().map(e -> new ReservaResponse(e)).toList();
        return reservasFiltradas;
    }

    public List<ReservaResponse> filtrarReservaPorCheckin(LocalDate dataInicio) {
        List<Reserva> reservas = reservaRepository.findByDataEntrada(dataInicio);
        List<ReservaResponse> reservasFiltradas = reservas.stream().map(e -> new ReservaResponse(e)).toList();
        return reservasFiltradas;
    }

    public void deleteReserva(Integer id) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        if (reservaOptional.isEmpty()) {
            throw new ResourceNotFoundException("Reserva não encontrada!");
        }
        reservaRepository.delete(reservaOptional.get());
    }

}
