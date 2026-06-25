package com.library.library_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções da aplicação.
 *
 * <p>Intercepta exceções lançadas em qualquer camada e retorna respostas
 * padronizadas com {@code timestamp}, {@code message} e {@code status}.</p>
 *
 * <p>Mapeamento de exceções para status HTTP:</p>
 * <table border="1">
 *   <caption>Exceções tratadas</caption>
 *   <tr><th>Exceção</th><th>Status HTTP</th></tr>
 *   <tr><td>{@link BookNotFoundException}</td><td>404 Not Found</td></tr>
 *   <tr><td>{@link AuthorNotFoundException}</td><td>404 Not Found</td></tr>
 *   <tr><td>{@link ConvertFileException}</td><td>422 Unprocessable Content</td></tr>
 *   <tr><td>{@link UnsupportedFileException}</td><td>415 Unsupported Media Type</td></tr>
 *   <tr><td>{@link ExternalAPIException}</td><td>502 Bad Gateway</td></tr>
 *   <tr><td>{@link InvalidISBNException}</td><td>400 Bad Request</td></tr>
 *   <tr><td>{@link InvalidKeyException}</td><td>400 Bad Request</td></tr>
 *   <tr><td>{@link InvalidIDException}</td><td>400 Bad Request</td></tr>
 *   <tr><td>{@link InvalidFileException}</td><td>400 Bad Request</td></tr>
 *   <tr><td>{@link InvalidFileNameException}</td><td>400 Bad Request</td></tr>
 * </table>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Trata {@link BookNotFoundException} → {@code 404 Not Found}. */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerBookNotFoundException(BookNotFoundException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /** Trata {@link AuthorNotFoundException} → {@code 404 Not Found}. */
    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerAuthorNotFoundException(AuthorNotFoundException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /** Trata {@link ConvertFileException} → {@code 422 Unprocessable Content}. */
    @ExceptionHandler(ConvertFileException.class)
    public ResponseEntity<Map<String, Object>> handlerConvertFileException(ConvertFileException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.UNPROCESSABLE_CONTENT.value());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(body);
    }

    /** Trata {@link UnsupportedFileException} → {@code 415 Unsupported Media Type}. */
    @ExceptionHandler(UnsupportedFileException.class)
    public ResponseEntity<Map<String, Object>> handlerUnsupportedFileException(UnsupportedFileException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(body);
    }

    /** Trata {@link ExternalAPIException} → {@code 502 Bad Gateway}. */
    @ExceptionHandler(ExternalAPIException.class)
    public ResponseEntity<Map<String, Object>> handlerExternalAPIException(ExternalAPIException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_GATEWAY.value());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    /** Trata {@link InvalidISBNException} → {@code 400 Bad Request}. */
    @ExceptionHandler(InvalidISBNException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidISBNException(InvalidISBNException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** Trata {@link InvalidKeyException} → {@code 400 Bad Request}. */
    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidKeyException(InvalidKeyException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** Trata {@link InvalidIDException} → {@code 400 Bad Request}. */
    @ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidIDException(InvalidIDException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** Trata {@link InvalidFileException} → {@code 400 Bad Request}. */
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidFileException(InvalidFileException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** Trata {@link InvalidFileNameException} → {@code 400 Bad Request}. */
    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<Map<String, Object>> handlerInvalidFileNameException(InvalidFileNameException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
