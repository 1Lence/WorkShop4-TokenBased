package org.example.workshop4tokenbased.controller.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.controller.exception.dto.ErrorResponse;
import org.example.workshop4tokenbased.controller.exception.dto.ErrorResponseBuilder;
import org.example.workshop4tokenbased.controller.exception.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(errorResponseBuilder.mapWithoutValid(UNAUTHORIZED, e.getMessage(), getUrl(request)),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleMaxThreadPoolException(AuthenticationException e, WebRequest request) {
        return new ResponseEntity<>(errorResponseBuilder.mapWithoutValid(UNAUTHORIZED, e.getMessage(), getUrl(request)),
                UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleAndroidCorrectnessException(MethodArgumentNotValidException e,
                                                                           WebRequest request) {
        //Не стал выносить в отдельный класс потому что используется всего в одном методе
        List<ValidationError> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fields -> ValidationError
                        .builder()
                        .field(fields.getField())
                        .message(fields.getDefaultMessage())
                        .build())
                .toList();

        return new ResponseEntity<>(errorResponseBuilder.mapWithValid(BAD_REQUEST, getUrl(request),validationErrors),
                BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleAndroidCorrectnessException(EntityExistsException e, WebRequest request) {
        return new ResponseEntity<>(errorResponseBuilder.mapWithoutValid(CONFLICT, e.getMessage(), getUrl(request)),
                CONFLICT);
    }

    private String getUrl(WebRequest request){
        return request.getDescription(false).replace("uri=", "");
    }
}