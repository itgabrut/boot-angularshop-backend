package controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Created by ilya on 28.01.2018.
 */
@ControllerAdvice
public class Advice {

    @ExceptionHandler(value = Throwable.class)
    public String gotNotFound(Exception ex){
        return "redirect:/";
    }
}
