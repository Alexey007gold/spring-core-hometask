package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.EventDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public interface EventDateService extends AbstractDomainObjectService<EventDate> {

    Collection<EventDate> getByDate(LocalDate date);

    Collection<EventDate> getByDateTime(LocalDateTime dateTime);

    Collection<EventDate> getByAuditoriumName(String name);

    Collection<EventDate> getByAuditorium(Auditorium auditorium);
}
