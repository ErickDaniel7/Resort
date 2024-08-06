package com.xpto.resort.service;

import com.xpto.resort.exceptions.*;
import com.xpto.resort.model.Quarto;
import com.xpto.resort.model.StatusQuarto;
import com.xpto.resort.repository.QuartoRepository;
import com.xpto.resort.service.dto.quarto.FiltroQuarto;
import com.xpto.resort.service.dto.quarto.QuartoCreateDto;
import com.xpto.resort.service.dto.quarto.QuartoResponseDto;
import com.xpto.resort.service.dto.quarto.QuartoUpdateDto;
import com.xpto.resort.service.util.mapper.QuartoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }


    @Transactional
    public QuartoResponseDto createQuarto(QuartoCreateDto quartoDto) {
        try {
            Quarto quarto = QuartoMapper.Instance.toCreateEntity(quartoDto);
            validateQuarto(quarto);
            quarto.setStatus(StatusQuarto.DISPONIVEL);
            if (quartoDto.vistaMar() == null) quarto.setVistaMar(false);
            if (quarto.getVistaMar() == true)
                quarto.setValorDia(quarto.getValorDia().multiply(BigDecimal.valueOf(2.0)));
            Quarto quartoResponse = quartoRepository.save(quarto);
            return QuartoMapper.Instance.toDto(quartoResponse);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("violates unique constraint")) {
                throw new UniqueConstraintViolationException("Já existe um quarto com mesmo nome");
            }
            throw new RuntimeException("Um erro ocorreu ao tentar salvar o quarto :" + message);
        }
    }

    @Transactional
    public QuartoResponseDto updateQuarto(QuartoUpdateDto quartoUpdateDto, Integer id) {

        try {
            Optional<Quarto> quartoOptional = quartoRepository.findById(id);
            if (quartoOptional.isPresent()) {
                Quarto quarto = quartoOptional.get();
                quarto.setValorDia(quartoUpdateDto.vistaMar() == false ? quartoUpdateDto.valorDia() : quartoUpdateDto.valorDia().multiply(BigDecimal.valueOf(2.0)));//Se vistamar valor é 2x
                quarto.setVistaMar(quartoUpdateDto.vistaMar() == null ? false : quartoUpdateDto.vistaMar());
                quarto.setNome(quartoUpdateDto.nome());
                quarto.setCapacidade(quartoUpdateDto.capacidade());
                quarto.setDescricao(quartoUpdateDto.descricao());

                if (filtrar(new FiltroQuarto(null, "OCUPADO", null))
                        .stream()
                        .filter(e -> e.id().equals(quarto.getId()))
                        .collect(Collectors.toList()).size() > 0
                )
                    quarto.setStatus(StatusQuarto.OCUPADO);
                else
                    quarto.setStatus(StatusQuarto.DISPONIVEL);

                validateQuarto(quarto);

                Quarto quartoResponse = quartoRepository.save(quarto);
                return QuartoMapper.Instance.toDto(quartoResponse);
            }
            throw new ResourceNotFoundException("Quarto não encontrado");
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("violates unique constraint")) {
                throw new UniqueConstraintViolationException("Já existe um quarto com mesmo nome");
            }
            throw new RuntimeException("Um erro ocorreu ao tentar salvar o quarto :" + message);
        }


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
                .map(e -> QuartoMapper.Instance.toDto(e))
                .collect(Collectors.toList());
    }

    // Deletar quarto por ID
    public void deleteQuarto(Integer id) {
        try {
            Optional<Quarto> quartoOptional = quartoRepository.findById(id);

            if (quartoOptional.isEmpty()) {
                throw new ResourceNotFoundException("quarto não encontrado");
            }

            quartoRepository.delete(quartoOptional.get());
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("violates foreign key constraint")) {
                throw new ForeignKeyConstraintViolationException("Não é possível deletar o quarto pois existem reservas associadas a ele.");
            }
            throw new RuntimeException("Erro inesperado ao tentar excluir o Quarto!"); // relançar outras exceções
        }
    }

    public List<QuartoResponseDto> getQuartoVago(LocalDate dataEntrada, LocalDate dataSaida) {
        return quartoRepository.findAvailableRooms(dataEntrada, dataSaida)
                .stream()
                .map(e -> QuartoMapper.Instance.toDto(e))
                .collect(Collectors.toList());
    }

    // Validar quarto
    private void validateQuarto(Quarto quarto) {

        Optional<Quarto> optionalquarto = quartoRepository.findByNome(quarto.getNome());
        if (optionalquarto.isPresent()) {
            Quarto quartoPesquisado = optionalquarto.get();
            if (!quartoPesquisado.getId().equals(quarto.getId())) {
                throw new UniqueConstraintViolationException("Já existe um quarto com o mesmo nome");
            }

        }

        if (quarto.getCapacidade() <= 0) {
            throw new RegraDeNegocioException("Quantidade máxima de ocupantes inválida");
        }

        if (quarto.getValorDia().intValue() <= 0) {
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
