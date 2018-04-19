package ua.epam.spring.hometask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam Map<String,String> allRequestParams) {
        ModelAndView mav = new ModelAndView("login");
        mav.addAllObjects(allRequestParams);
        return mav;
    }
}
