package ua.epam.spring.hometask.soap.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserService;
import ua.epam.spring.hometask.soap.message.*;

import static ua.epam.spring.hometask.configuration.SoapConfig.NAMESPACE_URI;
import static ua.epam.spring.hometask.soap.message.Result.FAIL;
import static ua.epam.spring.hometask.soap.message.Result.SUCCESS;


@Endpoint
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        User user = userService.getUserByLogin(request.getLogin());
        GetUserResponse response = new GetUserResponse();
        response.setUser(user);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setBirthDate(request.getBirthDate());
        try {
            userService.save(user);
            response.setResult(SUCCESS);
        } catch (Exception e) {
            response.setResult(FAIL);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        User user = userService.getUserByLogin(request.getLogin());
        if (user != null) {
            userService.remove(user);
            response.setResult(SUCCESS);
        } else {
            response.setResult(FAIL);
        }
        return response;
    }
}