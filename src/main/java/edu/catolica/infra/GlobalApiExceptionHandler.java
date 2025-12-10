package edu.catolica.infra;

import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.exception.consulta.*;
import edu.catolica.exception.usuario.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", ")) + ".";

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            ClinicaInexistenteException.class,
            UsuarioInexistenteException.class,
            ConsultaInexistenteException.class
    })
    public ResponseEntity<String> handleResourceNotFound(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            EmailDuplicadoException.class,
            ConsultaDuplicadaException.class
    })
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            CredenciaisInvalidasException.class,
            SessaoInvalidaException.class
    })
    public ResponseEntity<String> handleUnauthorized(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<String> handleForbidden(AcessoNegadoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            StatusInvalidoConsultaException.class,
            UsuarioInvalidoException.class,
            UsuariosClinicasDistintasException.class,
            JustificativaAusenteException.class
    })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerError(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}