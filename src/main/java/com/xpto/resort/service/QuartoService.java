package com.xpto.resort.service;

import com.xpto.resort.exceptions.RegraDeNegocioException;
import com.xpto.resort.exceptions.ResourceNotFoundException;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.StatusQuarto;
import com.xpto.resort.repository.QuartoRepository;
import com.xpto.resort.service.dto.quarto.FiltroQuarto;
import com.xpto.resort.service.dto.quarto.QuartoResponseDto;
import com.xpto.resort.service.dto.quarto.QuartoUpdateDto;
import com.xpto.resort.service.util.mapper.QuartoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuartoService {

    private QuartoRepository quartoRepository;

    @Autowired
    public QuartoService(QuartoRepository quartoRepository){
        this.quartoRepository = quartoRepository;
    }

    public QuartoResponseDto createQuarto(QuartoUpdateDto quartoDto){
        Quarto quarto = QuartoMapper.Instance.toEntity(quartoDto);
        validateQuarto(quarto);
        Quarto quartoResponse = quartoRepository.save(quarto);
        return QuartoMapper.Instance.toDto(quartoResponse);
    }

    @Transactional
    public QuartoResponseDto saveQuarto(QuartoUpdateDto quartoDto){
        Quarto quarto = QuartoMapper.Instance.toEntity(quartoDto);
        validateQuarto(quarto);
        if (quartoDto.status() == null) quarto.setStatus(StatusQuarto.DISPONIVEL);
        if (quartoDto.vistaMar() == null) quarto.setVistaMar(false);
        System.out.println(quarto);
        if (quarto.getVistaMar() ==true) quarto.setValorDia(quarto.getValorDia().multiply(BigDecimal.valueOf(2.0)));
        if (quartoDto.id()!=null){
            Optional<Quarto> quartoOptional = quartoRepository.findById(quartoDto.id());
            if (quartoOptional.isEmpty()){
                throw new ResourceNotFoundException("quarto não encontrado");
            }
        }
        Quarto quartoResponse = quartoRepository.save(quarto);
        return QuartoMapper.Instance.toDto(quartoResponse);
    }

    // Buscar quarto por ID
    public QuartoResponseDto findQuartoById(Integer id) {
        Optional<Quarto> quartoOptional = quartoRepository.findById(id);

        if (quartoOptional.isEmpty()) {
            throw new ResourceNotFoundException("quarto não encontrado");
        }

        return QuartoMapper.Instance.toDto(quartoOptional.get());
    }

    // Buscar todos os quartos
    public List<QuartoResponseDto> findAllQuartos() {
        return quartoRepository.findAll()
                .stream()
                .map(e->QuartoMapper.Instance.toDto(e))
                .collect(Collectors.toList());
    }

    // Deletar quarto por ID
    public void deleteQuarto(Integer id) {
        Optional<Quarto> quartoOptional = quartoRepository.findById(id);

        if (quartoOptional.isEmpty()) {
            throw new ResourceNotFoundException("quarto não encontrado");
        }

        quartoRepository.delete(quartoOptional.get());
    }

    public List<QuartoResponseDto> getQuartoVago(LocalDate dataEntrada, LocalDate dataSaida) {
        return quartoRepository.findAvailableRooms(dataEntrada, dataSaida)
                .stream()
                .map(e->QuartoMapper.Instance.toDto(e))
                .collect(Collectors.toList());
    }

    // Validar quarto
    private void validateQuarto(Quarto quartoDto) {
        if (quartoDto.getCapacidade() <= 0) {
            throw new RegraDeNegocioException("Quantidade máxima de ocupantes inválida");
        }

        if (quartoDto.getValorDia().intValue() <= 0) {
            throw new RegraDeNegocioException("Valor do quarto inválido");
        }
    }

    public List<QuartoResponseDto> filtrar(FiltroQuarto filtro) {
        LocalDate hoje = LocalDate.now();
        LocalDate fim = hoje.plusDays(2);

        // Obter todos os quartos de acordo com os filtros dinâmicos
        List<Quarto> quartos = quartoRepository.findQuartosDinamico(
                filtro.vistaMar(), filtro.quantidadeQuartos());

        // Obter todos os quartos vagos entre hoje e fim
        List<Quarto> quartosVagos = quartoRepository.findAvailableRooms(hoje, fim);

        // Marcar o status dos quartos com base na disponibilidade
        for (Quarto quarto : quartos) {
            if (quartosVagos.contains(quarto)) {
                quarto.setStatus(StatusQuarto.DISPONIVEL);
            } else {
                quarto.setStatus(StatusQuarto.OCUPADO);
            }
        }

        // Aplicar filtro de status, se necessário
        if (filtro.status() != null) {
            String statusFiltro = filtro.status().toUpperCase(); // Garantir que o filtro seja em maiúsculas
            quartos = quartos.stream()
                    .filter(quarto -> quarto.getStatus().name().equals(statusFiltro))
                    .collect(Collectors.toList());
        }

        // Mapear para DTOs e retornar
        return quartos.stream()
                .map(QuartoMapper.Instance::toDto)
                .collect(Collectors.toList());
    }
}
