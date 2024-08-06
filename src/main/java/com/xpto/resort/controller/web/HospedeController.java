package com.xpto.resort.controller.web;

import com.xpto.resort.service.HospedeService;
import com.xpto.resort.service.dto.hospede.HospedeCreateDto;
import com.xpto.resort.service.dto.hospede.HospedeUpdateDto;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/resort")
public class HospedeController {

    @Autowired
    HospedeService hospedeService;

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping("/hospede")
    public ModelAndView exibirTelaHospede(){
        ModelAndView modelAndView = new ModelAndView("hospedes");
        List<HospedeResponse> hospedes = hospedeService.findAll();
        modelAndView.addObject("hospedes", hospedes);
        return modelAndView;
    }

    @PostMapping("/hospede")
    public ResponseEntity criarNovoHospede(@RequestBody @Valid HospedeCreateDto hospedeUpdateDto) {
        HospedeResponse  response = hospedeService.createHospede(hospedeUpdateDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/hospede/{id}")
    public ResponseEntity atualizarQuarto(@RequestBody @Valid HospedeUpdateDto hospedeUpdateDto, @PathVariable Integer id) {
        HospedeResponse  response = hospedeService.updateHospede(hospedeUpdateDto, id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/hospede/{id}") // This route handles DELETE requests with an id path variable
    public ResponseEntity<Void> deleteHospede(@PathVariable Integer id) {
        hospedeService.deleteHospede(id);
        return ResponseEntity.noContent().build(); // Return a 204 No Content response
    }

    @GetMapping("/hospede/{id}")
    public ResponseEntity<HospedeResponse> obterHospede(@PathVariable Integer id){
        return ResponseEntity.ok(hospedeService.findById(id));
    }

    @GetMapping("/hospede/{id}/filtro")
    public ModelAndView filtroHospede(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView("hospedes");
        List<HospedeResponse> hospedes = hospedeService.filterById(id);
        modelAndView.addObject("hospedes", hospedes);
        return modelAndView;
    }
}
