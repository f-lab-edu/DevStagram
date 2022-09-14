package com.moondysmell.gateway.common;

//CommonCode에 명시되지 않은 Error. CustomException으로 처리할 수 없다.
public class UndefiedException extends RuntimeException {

    public UndefiedException(String message) {
        super(message);
    }

}