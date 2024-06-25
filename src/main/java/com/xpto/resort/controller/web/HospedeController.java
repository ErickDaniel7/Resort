package com.xpto.resort.controller.web;

import com.xpto.resort.service.HospedeService;
import com.xpto.resort.service.dto.hospede.HospedeInput;
import com.xpto.resort.service.dto.hospede.HospedeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ModelAndView listQuarto(){
        ModelAndView modelAndView = new ModelAndView("hospedes");
        List<HospedeResponse> hospedes = hospedeService.findAll();
        modelAndView.addObject("hospedes", hospedes);
        return modelAndView;
    }

    @PostMapping("/hospede")
    public ModelAndView criarNovoQuarto(@ModelAttribute HospedeInput hospedeInput, Model model) {

        hospedeService.save(hospedeInput);
        List<HospedeResponse> hospedes = hospedeService.findAll();
        ModelAndView modelAndView = new ModelAndView("hospedes");
        modelAndView.addObject("hospedes", hospedes);
        return modelAndView;
    }

    @DeleteMapping("/hospede/{id}") // This route handles DELETE requests with an id path variable
    public ResponseEntity<Void> deleteQuarto(@PathVariable Integer id) {
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
