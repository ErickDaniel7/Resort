package com.xpto.resort.controller.web;


import com.xpto.resort.service.ReservaService;
import com.xpto.resort.service.dto.reserva.ReservaInput;
import com.xpto.resort.service.dto.reserva.ReservaResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("resort")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @GetMapping("/reserva")
    public ModelAndView exibirTelaReservas(){
        List<ReservaResponse> reservas = reservaService.findAll();
        System.out.println(reservas);
        ModelAndView modelAndView = new ModelAndView("reservas");
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }

    @PostMapping("reserva")
    public ResponseEntity<?> criarNovoReserva(@RequestBody @Valid ReservaInput reservaInput, UriComponentsBuilder uriComponentsBuilder) {
        ReservaResponse response =  reservaService.save(reservaInput);
        var uri = uriComponentsBuilder.path("reserva/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/reserva/{id}")
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
    public ResponseEntity filtrarReservas(@PathVariable Integer id){
        List<ReservaResponse> reservas = reservaService.filtrarReservaPorId(id);
        return ResponseEntity.ok().body(reservas);
    }

    @GetMapping("reserva/hospede/{hospede}")
    public ResponseEntity filtrarReservasPorHospede(@PathVariable String hospede){
        List<ReservaResponse> reservas = reservaService.filtrarReservaPorHospede(hospede);
        return ResponseEntity.ok().body(reservas);
    }

    @GetMapping("reserva/checkin/{checkin}")
    public ResponseEntity filtrarReservasPorHospede(@PathVariable LocalDate checkin){
        List<ReservaResponse> reservas = reservaService.filtrarReservaPorCheckin(checkin);
        return ResponseEntity.ok().body(reservas);
    }

    @PutMapping("/reserva/{id}/checkin")
    public ResponseEntity<Void> realizarCheckin(@PathVariable Integer id){
        System.out.println("ID RECEBIDO ::: "+id);
        reservaService.checkin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reserva/{id}/checkout")
    public ResponseEntity<Void> realizarCheckout(@PathVariable Integer id){
        reservaService.checkout(id);
        return ResponseEntity.noContent().build();
    }
}
