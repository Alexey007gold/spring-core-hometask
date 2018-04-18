package ua.epam.spring.hometask.controller.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Oleksii_Kovetskyi on 4/18/2018.
 */
@ControllerAdvice
@Controller
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ModelAndView myError(Exception exception) {
        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("message", exception.getMessage());
        return mav;
    }
}
