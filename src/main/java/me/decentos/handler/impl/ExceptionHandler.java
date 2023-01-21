package me.decentos.handler.impl;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Aspect
@Component
public class ExceptionHandler {

    @AfterThrowing(pointcut = "execution(* me.decentos.*.*.*(..))", throwing = "ex")
    public void exceptionHandler(TelegramApiException ex) {
        ex.printStackTrace();
    }
}
