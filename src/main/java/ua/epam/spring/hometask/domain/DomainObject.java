package ua.epam.spring.hometask.domain;

import javax.xml.bind.annotation.*;

/**
 * @author Yuriy_Tkach
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "domainObject")
@XmlSeeAlso({
        Ticket.class,
        Event.class,
        User.class,
        EventDate.class
})
public class DomainObject {

    @XmlTransient
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
