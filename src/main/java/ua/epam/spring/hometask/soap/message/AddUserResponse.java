package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/5/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addUserResponse", propOrder = {"result"})
@XmlRootElement(name = "addUserResponse")
public class AddUserResponse {

    @XmlElement(required = true)
    protected Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
