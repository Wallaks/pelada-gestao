package com.pelada_gestao.exception;

import com.pelada_gestao.domain.model.response.ErrorResponse;
import com.pelada_gestao.exception.custom.AcessoNegadoException;
import com.pelada_gestao.exception.custom.EntidadeEmUsoException;
import com.pelada_gestao.exception.custom.NomeDeJogadorDuplicadoException;
import com.pelada_gestao.exception.custom.NomeDeSorteioDuplicadoException;
import com.pelada_gestao.exception.custom.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AcessoNegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<ErrorResponse> handleEntidadeEmUso(EntidadeEmUsoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro inesperado. Contate o suporte.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(NomeDeJogadorDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleNomeDeJogadorDuplicado(NomeDeJogadorDuplicadoException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(NomeDeSorteioDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleNomeDeSorteioDuplicado(NomeDeSorteioDuplicadoException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }


}
