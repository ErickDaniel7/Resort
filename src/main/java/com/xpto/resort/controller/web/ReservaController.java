package com.xpto.resort.controller.web;


import com.xpto.resort.service.ReservaService;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("resort")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @GetMapping("/reserva")
    public ModelAndView listreserva(){
        List<ReservaResponse> reservas = reservaService.findAll();
        System.out.println(reservas);
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @PostMapping("/reserva")
    public ModelAndView criarNovoReserva(@ModelAttribute ReservaInput reservaInput, Model model) {
        System.out.println(reservaInput);
        reservaService.save(reservaInput);
        List<ReservaResponse> reservas = reservaService.findAll();
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @DeleteMapping("/reserva/{id}") // This route handles DELETE requests with an id path variable
    public ResponseEntity<Void> deletereserva(@PathVariable Integer id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build(); // Return a 204 No Content response
    }

    @GetMapping("reserva/{id}")
    public ResponseEntity<?> obterUmaReserva(@PathVariable Integer id){
        ReservaResponse reserva = reservaService.findById(id);
        return ResponseEntity.ok(reserva);
    }
    @GetMapping("reserva/filtro/{id}")
    public ModelAndView filtrarReservas(@PathVariable Integer id){
        List<ReservaResponse> reservas = reservaService.filtrarReservaPorId(id);
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @GetMapping("reserva/hospede/{hospede}")
    public ModelAndView filtrarReservasPorHospede(@PathVariable String hospede){

        List<ReservaResponse> reservas = reservaService.filtrarReservaPorHospede(hospede);
        System.out.println(hospede);
        System.out.println(reservas);
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @GetMapping("reserva/checkin/{checkin}")
    public ModelAndView filtrarReservasPorHospede(@PathVariable LocalDate checkin){
        List<ReservaResponse> reservas = reservaService.filtrarReservaPorCheckin(checkin);
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @PostMapping("/reserva/{id}/checkin")
    public ResponseEntity<Void> realizarCheckin(@PathVariable Integer id){
        System.out.println("ID RECEBIDO ::: "+id);
        reservaService.checkin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reserva/{id}/checkout")
    public ResponseEntity<Void> realizarCheckout(@PathVariable Integer id){
        reservaService.checkout(id);
        return ResponseEntity.noContent().build();
    }
}
