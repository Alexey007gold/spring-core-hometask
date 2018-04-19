package ua.epam.spring.hometask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.domain.UserRole;
import ua.epam.spring.hometask.service.interf.UserRoleService;
import ua.epam.spring.hometask.service.interf.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/15/2018.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private UserRoleService userRoleService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping
    public String getUsers(@ModelAttribute("model") ModelMap model,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String email) {
        if (email != null) {
            if (firstName != null || lastName != null)
                throw new IllegalArgumentException("First and last name should not be specified when email is specified");
        }
        model.addAttribute("userList", getUsersByFirstNameAndLastNameOrEmail(firstName, lastName, email));
        return "users";
    }

    @RequestMapping("/pdf")
    public String getUsersPdf(Model model,
                                    @RequestParam(required = false) String firstName,
                                    @RequestParam(required = false) String lastName,
                                    @RequestParam(required = false) String email) {
        if (email != null) {
            if (firstName != null || lastName != null)
                throw new IllegalArgumentException("First and last name should not be specified when email is specified");
        }
        model.addAttribute("userList", getUsersByFirstNameAndLastNameOrEmail(firstName, lastName, email));
        return "userPdfView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerUser(@RequestBody User user) {
        if (user.getEmail().contains("@")) {
            userService.save(user);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            LocalDate dateTime = new Timestamp(Long.parseLong(split[5]) * 1000).toLocalDateTime().toLocalDate();
            User user = new User();
            user.setFirstName(split[0]);
            user.setLastName(split[1]);
            user.setEmail(split[2]);
            user.setLogin(split[3]);
            user.setPassword(passwordEncoder.encode(split[4]));
            user.setBirthDate(dateTime);
            userService.save(user);

            List<UserRole> userRoles = new ArrayList<>();
            for (int i = 6; i < split.length; i++) {
                userRoles.add(new UserRole(user.getId(), split[i]));
            }
            userRoles.forEach(ur -> userRoleService.save(ur));
        }
        reader.close();
    }


    private List<User> getUsersByFirstNameAndLastNameOrEmail(String firstName, String lastName, String email) {
        if (email != null) {
            User userByEmail = userService.getUserByEmail(email);
            return userByEmail != null ? Collections.singletonList(userByEmail) : Collections.emptyList();
        }
        else if (firstName != null) {
            if (lastName != null) {
                return userService.getUsersByFirstAndLastName(firstName, lastName);
            } else {
                return userService.getUsersByFirstName(firstName);
            }
        } else if (lastName != null) {
            return userService.getUsersByLastName(lastName);
        } else {
            return Collections.emptyList();
        }
    }
}
