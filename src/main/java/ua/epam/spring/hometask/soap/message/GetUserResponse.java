package ua.epam.spring.hometask.soap.message;

import ua.epam.spring.hometask.domain.User;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserResponse")
@XmlRootElement(name = "getUserResponse")
public class GetUserResponse {

    @XmlElement(required = true)
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
