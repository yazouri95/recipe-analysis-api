package com.wiley.recipe.exception.handler;

import com.wiley.recipe.dto.ProblemDetailDTO;
import com.wiley.recipe.domain.exception.DomainException;
import com.wiley.recipe.domain.exception.RecipeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetailDTO> handleGlobalException(Exception ex, WebRequest request) {
        ProblemDetailDTO problemDetail = toProblemDetailDto("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, request);
        logError(problemDetail.getError(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ProblemDetailDTO> handleDomainException(DomainException ex, WebRequest request) {
        ProblemDetailDTO problemDetail = toProblemDetailDto(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        logError(problemDetail.getError(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetailDTO> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        ProblemDetailDTO problemDetail = toProblemDetailDto("no routes found.", HttpStatus.NOT_FOUND, request);
        logError(problemDetail.getError(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetailDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        ProblemDetailDTO problemDetail = toProblemDetailDto(ex.getBody().getDetail(), HttpStatus.BAD_REQUEST, request);
        logError(problemDetail.getError(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetailDTO> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getConstraintViolations()
                .forEach(cv -> sb.append(cv.getPropertyPath().toString()).append(" ").append(cv.getMessage()).append(", "));

        ProblemDetailDTO problemDetail = toProblemDetailDto(sb.toString(), HttpStatus.BAD_REQUEST, request);
        logError(problemDetail.getError(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private ProblemDetailDTO toProblemDetailDto(String errorMessage, HttpStatus httpStatus, WebRequest request) {
        return ProblemDetailDTO
                .builder()
                .path(request.getDescription(false))
                .error(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void logError(String message, Exception e) {
        log.error(message, e);
    }
}
