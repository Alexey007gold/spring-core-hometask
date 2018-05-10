package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/5/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteUserResponse", propOrder = {"result"})
@XmlRootElement(name = "deleteUserResponse")
public class DeleteUserResponse {

    @XmlElement(required = true)
    protected Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
