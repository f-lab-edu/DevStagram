package com.moondysmell.gateway.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    //CommonCode에 명시된 에러
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
    protected ResponseEntity<CommonResponse<String>> handleNoSuchElementException(Exception e) {
        CommonResponse<String> response= new CommonResponse(CommonCode.FAIL, e.getMessage(), null);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // CommonCode에 명시되지 않은 Exception 발생시 처리
    @ExceptionHandler(value = UndefiedException.class)
    protected ResponseEntity<CommonResponse<String>> handleUnDefinedExceptionHandler(Exception e) {
        CommonResponse<String> response= new CommonResponse(CommonCode.FAIL, e.getMessage(), null);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
