package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.interf.EventDateDAO;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.service.interf.EventDateService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Service
public class EventDateServiceImpl extends AbstractDomainObjectServiceImpl<EventDate, EventDateDAO>
        implements EventDateService {

    public EventDateServiceImpl(EventDateDAO domainObjectDAO) {
        super(domainObjectDAO);
    }

    @Override
    public Collection<EventDate> getByDate(LocalDate date) {
        return domainObjectDAO.getAll().stream()
                .filter(e -> LocalDate.from(e.getDateTime()).isEqual(date))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<EventDate> getByDateTime(LocalDateTime dateTime) {
        return domainObjectDAO.getAll().stream()
                .filter(e -> e.getDateTime().isEqual(dateTime))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<EventDate> getByAuditoriumName(String name) {
        return domainObjectDAO.getBy(new String[] {"auditorium_name"}, name);
    }

    @Override
    public Collection<EventDate> getByAuditorium(Auditorium auditorium) {
        return getByAuditoriumName(auditorium.getName());
    }
}
