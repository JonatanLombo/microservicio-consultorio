package com.proyecto.pacientes.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;

/**
 * {@code GlobalExceptionHandler} es una clase de manejo global de excepciones
 * para todos los controladores REST de la aplicación.
 *
 * <p>Su objetivo es capturar y gestionar de forma centralizada las excepciones
 * que puedan producirse durante el procesamiento de peticiones HTTP, 
 * devolviendo respuestas JSON con códigos de estado HTTP apropiados.</p>
 *
 * <p>Entre las excepciones gestionadas se incluyen errores de validación 
 * de campos anotados con {@code @Valid} y {@code @NotNull}, así como errores
 * generales no controlados.</p>
 *
 * <p>Esta clase contribuye a mantener una arquitectura limpia, evitando 
 * duplicar bloques {@code try-catch} en los controladores y asegurando 
 * una comunicación uniforme de errores hacia el cliente.</p>
 *
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler{

    /**
     * Maneja las excepciones generadas cuando fallan las validaciones 
     * de los campos anotados con {@code @Valid} en las entidades o DTOs.
     *
     * <p>Captura los errores de tipo {@link MethodArgumentNotValidException}
     * y devuelve un mapa con los nombres de los campos y los mensajes 
     * de error correspondientes.</p>
     *
     * @param ex la excepción capturada
     * @return un {@link ResponseEntity} con código 400 (Bad Request) y los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }


    /**
     * Maneja las excepciones generadas por violaciones de restricciones 
     * de validación, como {@code @NotNull}, {@code @NotBlank}, etc.
     *
     * <p>Captura errores de tipo {@link ConstraintViolationException}
     * y devuelve un mensaje descriptivo con estado HTTP 400.</p>
     *
     * @param ex la excepción capturada
     * @return un {@link ResponseEntity} con código 400 y el mensaje de error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    /**
     * Maneja cualquier otra excepción no controlada que pueda ocurrir 
     * durante la ejecución de la aplicación.
     *
     * <p>Devuelve un mensaje genérico con código HTTP 500 
     * (Internal Server Error) para evitar exponer detalles internos.</p>
     *
     * @param ex la excepción capturada
     * @return un {@link ResponseEntity} con código 500 y un mensaje general de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }


    /**
     * Maneja excepciones lanzadas cuando un recurso no existe.
     *
     * @param ex excepción lanzada al no encontrar el recurso.
     * @return respuesta HTTP 404 con el mensaje correspondiente.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
