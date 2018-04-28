package ua.epam.spring.hometask.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Oleksii_Kovetskyi on 4/27/2018.
 */
@Controller
public class HomeController {

    @RequestMapping({"/home", "/", ""})
    public String getComingEvents(@ModelAttribute("model") ModelMap model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        model.addAttribute("admin", isAdmin);
        return "home";
    }
}
