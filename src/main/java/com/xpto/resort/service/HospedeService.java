package com.xpto.resort.service;

import com.xpto.resort.exceptions.*;
import com.xpto.resort.model.Hospede;
import com.xpto.resort.repository.HospedeRepository;
import com.xpto.resort.service.dto.hospede.HospedeCreateDto;
import com.xpto.resort.service.dto.hospede.HospedeUpdateDto;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import com.xpto.resort.service.util.mapper.HospedeMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospedeService {

    private HospedeRepository hospedeRepository;

    @Autowired
    public HospedeService(HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
    }

    @Transactional
    public HospedeResponse save(HospedeUpdateDto hospedeUpdateDto) {
        Hospede hospede = HospedeMapper.instance.toUpdateEntity(hospedeUpdateDto);
        validateAge(hospede.getDataNascimento());
        if (hospedeUpdateDto.id() != null) {
            Optional<Hospede> hospedeOptional = hospedeRepository.findById(hospedeUpdateDto.id());
            if (hospedeOptional.isEmpty()) {
                throw new ResourceNotFoundException("Hospede não encontrado!");
            }
        }else {
            String cpf = hospedeUpdateDto.cpf();
            String rg = hospedeUpdateDto.rg();
            if (hospedeRepository.findByCpf(cpf).isPresent()) throw new UniqueConstraintViolationException("CPF já exite!");
            if (hospedeRepository.findByRg(rg).isPresent()) throw new UniqueConstraintViolationException("RG já exite!");
        }
        HospedeResponse response = HospedeMapper.instance
                .toDto(
                        hospedeRepository.save(hospede)
                );
        return response;
    }

    @Transactional
    public HospedeResponse createHospede(HospedeCreateDto dto){
        Hospede hospede = HospedeMapper.instance.toCreateEntity(dto);
        validateAge(hospede.getDataNascimento());
        String cpf = dto.cpf();
        String rg = dto.rg();
        if (hospedeRepository.findByCpf(cpf).isPresent()) throw new UniqueConstraintViolationException("CPF já exite!");
        if (hospedeRepository.findByRg(rg).isPresent()) throw new UniqueConstraintViolationException("RG já exite!");
        HospedeResponse response = HospedeMapper.instance
                .toDto(
                        hospedeRepository.save(hospede)
                );
        return response;
    }

    @Transactional
    public HospedeResponse updateHospede(HospedeUpdateDto dto, Integer id){
        if (dto.id().equals(id)) {
            Hospede hospede = HospedeMapper.instance.toUpdateEntity(dto);
            validateAge(hospede.getDataNascimento());
            Optional<Hospede> hospedeOptional = hospedeRepository.findById(dto.id());
            if (hospedeOptional.isEmpty()) {
                throw new ResourceNotFoundException("Hospede não encontrado!");
            }
            HospedeResponse response = HospedeMapper.instance
                    .toDto(
                            hospedeRepository.save(hospede)
                          );
                return response;
        }
        throw new IllegalArgumentException("ID do corpo da requisição é diferente do informado em URL");
    }

    private void validateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        int years = period.getYears();

        if (years < 18) {
            throw new RegraDeNegocioException("Idade do hospede deve ser maior ou igual a 18 anos.");
        }
    }

    public List<HospedeResponse> findAll() {
        return hospedeRepository.findAll()
                .stream().
                map(e -> HospedeMapper.instance.toDto(e))
                .collect(Collectors.toList());
    }

    public HospedeResponse findById(Integer id) {
        return HospedeMapper.instance.toDto(hospedeRepository.findById(id).get());
    }

    public void deleteHospede(Integer id) {
        Optional<Hospede> hospedeOptional = hospedeRepository.findById(id);

        if (hospedeOptional.isEmpty()) {
            throw new ResourceNotFoundException("Hóspede não encontrado");
        }
        try {
            hospedeRepository.delete(hospedeOptional.get());
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("violates foreign key constraint")) {
                throw new ForeignKeyConstraintViolationException("Não é possível deletar o hóspede pois existem reservas associadas a ele.");
            }
            else {
                throw new RuntimeException("Ocorreu um erro ao tentar remover o hóspede");
            }
        }
    }

    public List<HospedeResponse> filterById(Integer id) {
        Optional<Hospede> hospedeOptional = hospedeRepository.findById(id);
        List<HospedeResponse> hospedes = new ArrayList<>();
        if (hospedeOptional.isPresent()) {
            hospedes.add(HospedeMapper.instance.toDto(hospedeOptional.get()));
        }
        return hospedes;
    }

}
