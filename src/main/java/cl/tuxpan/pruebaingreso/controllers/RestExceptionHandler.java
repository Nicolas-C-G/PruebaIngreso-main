package cl.tuxpan.pruebaingreso.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Global REST exception handler for the application.
 * <p>
 * This class provides centralized exception handling for REST controllers,
 * allowing consistent error response structures across the API.
 * <p>
 * Currently, it handles {@link MethodArgumentNotValidException} thrown
 * when validation on a method argument annotated with {@code @Valid} fails.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles {@link MethodArgumentNotValidException} by returning a standardized
     * error response containing the HTTP status, error type, and detailed validation messages.
     * <p>
     * Each validation error entry in the {@code errors} list includes:
     * <ul>
     *   <li>{@code field} – the name of the field that failed validation</li>
     *   <li>{@code message} – the default error message for the violation</li>
     * </ul>
     *
     * @param ex the exception containing validation failure details
     * @return a {@link ResponseEntity} with a 400 (Bad Request) status code and
     *         a structured error body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();

        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }
}
