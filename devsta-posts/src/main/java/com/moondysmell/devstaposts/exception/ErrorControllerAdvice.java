package com.moondysmell.devstaposts.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    protected ResponseEntity<CommonResponse<String>> handleMethodArgumentNotValidException(BindException e) {
        log.error(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("; ");
        }
        CommonResponse<String> response= new CommonResponse(CommonCode.INVALID_ELEMENTS, stringBuilder.toString(), Map.of("errorTrace", e.getMessage()));
        // 400으로 리턴하면 gateway 에서 UndefiedException으로 처리, 200으로 리턴하면 CommonResponse 처리 (CommonResponse 형식 지켜야함)
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}