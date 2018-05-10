package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.*;
import java.time.LocalDate;

/**
 * Created by Oleksii_Kovetskyi on 5/5/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addUserRequest", propOrder = {"firstName",
        "lastName", "email", "birthDate", "login", "password"})
@XmlRootElement(name = "addUserRequest")
public class AddUserRequest {

    @XmlElement(required = true)
    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String login;

    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
