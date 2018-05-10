package ua.epam.spring.hometask.soap.message;

import ua.epam.spring.hometask.domain.Event;

import javax.xml.bind.annotation.*;

/**
 * Created by Oleksii_Kovetskyi on 5/5/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addEventRequest", propOrder = {"event"})
@XmlRootElement(name = "addEventRequest")
public class AddEventRequest {

    @XmlElement(required = true)
    protected Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
