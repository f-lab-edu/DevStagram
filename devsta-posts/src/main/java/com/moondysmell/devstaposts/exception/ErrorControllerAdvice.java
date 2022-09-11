package com.moondysmell.devstaposts.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<CommonResponse<String>> handleCustomException(CustomException e) {

        CommonResponse<String> response= new CommonResponse(e.getErrorCode());
        log.error(">>> " + response);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<String>> handleMethodArgumentNotValidException(Exception e) {
        log.error(e.getMessage());
        CommonResponse<String> response= new CommonResponse(CommonCode.INVALID_ELEMENTS, Map.of("errorTrace", e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}