package ua.epam.spring.hometask.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static ua.epam.spring.hometask.view.UserPdfView.USER_LIST;

/**
 * Created by Oleksii_Kovetskyi on 4/15/2018.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final String HEADER_TEXT = "headerText";
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    public String getUsers(Model model,
                           @RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false, defaultValue = "false") boolean pdf) {
        if (email != null && (firstName != null || lastName != null)) {
            throw new IllegalArgumentException("First and last name should not be specified when email is specified");
        }
        model.addAttribute(USER_LIST, getUsersByFirstNameAndLastNameOrEmail(firstName, lastName, email));
        model.addAttribute(HEADER_TEXT, "Users");

        return pdf ? "userPdfView" : "users";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerUser(@RequestBody User user) {
        if (user.getEmail().contains("@")) {
            userService.save(user);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadUsers(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        List<User> addedUsers = userService.parseUsersFromInputStream(file.getInputStream());

        model.addAttribute(USER_LIST, addedUsers);
        model.addAttribute(HEADER_TEXT, "Added Users");
        return "users";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadUsersPage() {
        return "upload_users";
    }


    private List<User> getUsersByFirstNameAndLastNameOrEmail(String firstName, String lastName, String email) {
        if (email != null) {
            User user = userService.getUserByEmail(email);
            return user != null ? Collections.singletonList(user) : Collections.emptyList();
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
