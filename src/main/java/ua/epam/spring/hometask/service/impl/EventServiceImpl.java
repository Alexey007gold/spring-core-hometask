package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.EventDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.interf.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class EventServiceImpl extends AbstractDomainObjectServiceImpl<Event, EventDAO> implements EventService {

    public EventServiceImpl(EventDAO eventDAO) {
        super(eventDAO);
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return domainObjectDAO.getByName(name);
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to) {
        return domainObjectDAO.getAll().stream()
                .filter(e -> e.getAirDates().keySet().stream()
                        .anyMatch(d -> (LocalDate.from(d).isAfter(from) || LocalDate.from(d).isEqual(from)) &&
                                (LocalDate.from(d).isBefore(to) || LocalDate.from(d).isEqual(to))))
                .collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Set<Event> getNextEvents(@Nonnull LocalDateTime to) {
        LocalDateTime from = LocalDateTime.now();
        return domainObjectDAO.getAll().stream()
                .filter(e -> e.getAirDates().keySet().stream()
                        .anyMatch(d -> d.isAfter(from) && d.isBefore(to)))
                .collect(Collectors.toSet());
    }
}
