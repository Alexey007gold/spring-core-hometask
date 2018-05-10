package ua.epam.spring.hometask.soap.message;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEventRequest", propOrder = {"name"})
@XmlRootElement(name = "getEventRequest")
public class GetEventRequest {

    @XmlElement(required = true)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
