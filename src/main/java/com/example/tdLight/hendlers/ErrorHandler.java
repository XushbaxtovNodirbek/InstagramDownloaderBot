package com.example.tdLight.hendlers;

import it.tdlight.common.ExceptionHandler;

public class ErrorHandler implements ExceptionHandler {
    @Override
    public void onException(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
