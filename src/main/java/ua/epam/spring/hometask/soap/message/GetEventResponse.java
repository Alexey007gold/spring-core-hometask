package ua.epam.spring.hometask.soap.message;

import ua.epam.spring.hometask.domain.Event;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/4/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEventResponse")
@XmlRootElement(name = "getEventResponse")
public class GetEventResponse {

    @XmlElement(required = true)
    protected Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
