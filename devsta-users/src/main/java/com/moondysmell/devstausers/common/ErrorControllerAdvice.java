package com.moondysmell.devstausers.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<CommonResponse<String>> handleCustomException(CustomException e) {
//        ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        CommonResponse<String> response= new CommonResponse(e.getErrorCode());
        //attribute 넣고싶다면 이렇게
        //response.setAttribute(Map.of("result", "실패"));
        log.error(">>> " + response);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<String> handleNoSuchElementException(Exception e) {
        log.error(e.getMessage());
        // 400으로 리턴하면 gateway 에서 UndefiedException으로 처리, 200으로 리턴하면 CustomException으로 처리 (CommonResponse 형식 지켜야함)
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<String>> handleMethodArgumentNotValidException(Exception e) {
        log.error(e.getMessage());
        Pattern pattern = Pattern.compile("(?<=default message \\[)(.*?)(?=\\])");
        Matcher matcher = pattern.matcher(e.getMessage());
        String result = "";
        while (matcher.find()) {
            result += matcher.group() + ";";
        }
        CommonResponse<String> response= new CommonResponse(CommonCode.INVALID_ELEMENTS, result, Map.of("errorTrace", e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
