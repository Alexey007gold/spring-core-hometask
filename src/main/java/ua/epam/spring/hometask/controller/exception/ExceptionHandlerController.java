package ua.epam.spring.hometask.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Oleksii_Kovetskyi on 4/18/2018.
 */
@ControllerAdvice
@RestController
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public String myError(Exception exception) {
        return exception.getMessage();
    }
}
