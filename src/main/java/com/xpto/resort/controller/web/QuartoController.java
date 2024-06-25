package com.xpto.resort.controller.web;

import com.xpto.resort.service.QuartoService;
import com.xpto.resort.service.dto.quarto.FiltroQuarto;
import com.xpto.resort.service.dto.quarto.QuartoResponseDto;
import com.xpto.resort.service.dto.quarto.QuartoUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/resort")
public class QuartoController {

    @Autowired
    QuartoService quartoService;

    @GetMapping("/quarto")
    public ModelAndView listQuarto(){
        List<QuartoResponseDto> quartos =  quartoService.filtrar(new FiltroQuarto(null,null,null));
        ModelAndView modelAndView = new ModelAndView("quartos");
        modelAndView.addObject("quartos", quartos);
        return modelAndView;
    }

    @GetMapping("/quarto/ocupacao/inicio/{dataInicio}/fim/{dataFim}")
    public ResponseEntity<?> getQuartoVago(@PathVariable LocalDate dataInicio, @PathVariable LocalDate dataFim){
        return ResponseEntity.ok(quartoService.getQuartoVago(dataInicio,dataFim));
    }

    @PostMapping("/quarto")
    public ResponseEntity<?> criarNovoQuarto(@ModelAttribute @Valid QuartoUpdateDto quartoInput, Model model) {
        quartoService.saveQuarto(quartoInput);
        List<QuartoResponseDto> quartos = quartoService.filtrar(new FiltroQuarto(null,null,null));
        return ResponseEntity.ok(quartos);
    }

    @DeleteMapping("/quarto/{id}")
    public ResponseEntity<Void> deleteQuarto(@PathVariable Integer id) {
        quartoService.deleteQuarto(id);
        return ResponseEntity.noContent().build(); // Return a 204 No Content response
    }

    @PostMapping("/quarto/filtro")
    public ResponseEntity<?> filtrarQuartos(@RequestBody FiltroQuarto filtro){
        List<QuartoResponseDto> quartos = quartoService.filtrar(filtro);
        return ResponseEntity.ok(quartos);
    }

}
