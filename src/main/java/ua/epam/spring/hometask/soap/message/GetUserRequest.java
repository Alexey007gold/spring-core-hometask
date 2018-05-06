package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserRequest", propOrder = {"login"})
@XmlRootElement(name = "getUserRequest")
public class GetUserRequest {

    @XmlElement(required = true)
    protected String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
