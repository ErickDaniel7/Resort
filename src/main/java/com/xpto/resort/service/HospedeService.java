package com.xpto.resort.service;

import com.xpto.resort.exceptions.RegraDeNegocioException;
import com.xpto.resort.exceptions.ResourceNotFoundException;
import com.xpto.resort.model.Hospede;
import com.xpto.resort.repository.HospedeRepository;
import com.xpto.resort.service.dto.hospede.HospedeInput;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import com.xpto.resort.service.util.mapper.HospedeMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public HospedeResponse save(HospedeInput hospedeInput) {
        Hospede hospede = HospedeMapper.instance.toEntity(hospedeInput);
        validateAge(hospede.getDataNascimento());
        if (hospedeInput.id() != null) {
            Optional<Hospede> hospedeOptional = hospedeRepository.findById(hospedeInput.id());
            if (hospedeOptional.isEmpty()) {
                throw new ResourceNotFoundException("Hospede não encontrado!");
            }
        }
        HospedeResponse response = HospedeMapper.instance
                .toDto(
                        hospedeRepository.save(hospede)
                );
        return response;
    }

    private void validateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        int years = period.getYears();

        if (years < 18) {
            throw new RegraDeNegocioException("Idade do hospede deve ser maior ou igual a 18 anos.");
        }
    }

    public List<HospedeResponse> findAll(){
        return hospedeRepository.findAll()
                .stream().
                map(e->HospedeMapper.instance.toDto(e))
                .collect(Collectors.toList());
    }

    public HospedeResponse findById(Integer id){
        return HospedeMapper.instance.toDto(hospedeRepository.findById(id).get());
    }

    public void deleteHospede(Integer id) {
        Optional<Hospede> hospedeOptional = hospedeRepository.findById(id);

        if (hospedeOptional.isEmpty()) {
            throw new ResourceNotFoundException("quarto não encontrado");
        }

        hospedeRepository.delete(hospedeOptional.get());
    }

    public List<HospedeResponse> filterById(Integer id){
        Optional<Hospede> hospedeOptional = hospedeRepository.findById(id);
        List<HospedeResponse> hospedes = new ArrayList<>();
        if (hospedeOptional.isPresent()) {
            hospedes.add(HospedeMapper.instance.toDto(hospedeOptional.get()));
        }
        return hospedes;
    }

}
