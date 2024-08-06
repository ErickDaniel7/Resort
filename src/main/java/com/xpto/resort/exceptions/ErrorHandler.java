package com.xpto.resort.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({EntityNotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity tratarRegraDeNegocio(RegraDeNegocioException ex) {
        var erro = ex.getMessage();
        return ResponseEntity.badRequest().body(new DadosErros(erro));
    }

    @ExceptionHandler(ForeignKeyConstraintViolationException.class)
    public ResponseEntity tratarChaveEstrangeira(ForeignKeyConstraintViolationException ex){
        var erro = ex.getMessage();
        return ResponseEntity.badRequest().body(new DadosErros(erro));
    }

    @ExceptionHandler(NotNullConstraintViolationException.class)
    public ResponseEntity tratarInsercaoNotNull(NotNullConstraintViolationException ex){
        var erro = ex.getMessage();
        return ResponseEntity.badRequest().body(new DadosErros(erro));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity notFound(ResourceNotFoundException ex) {
        var erro = ex.getMessage();
        return ResponseEntity.badRequest().body(new DadosErros(erro));
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity tratarDuplicacaoDeChaveUnica(UniqueConstraintViolationException ex) {
        var erro = ex.getMessage();
        return ResponseEntity.badRequest().body(new DadosErros(erro));
    }

    private record DadosErroValidacao(String field, String message) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }


    private record DadosErros(String message) {
    }

}