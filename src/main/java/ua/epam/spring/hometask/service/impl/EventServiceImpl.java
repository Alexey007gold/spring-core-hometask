package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.service.interf.AuditoriumService;
import ua.epam.spring.hometask.service.interf.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class EventServiceImpl extends AbstractDomainObjectServiceImpl<Event, EventDAO> implements EventService {

    private AuditoriumService auditoriumService;

    public EventServiceImpl(EventDAO domainObjectDAO, AuditoriumService auditoriumService) {
        super(domainObjectDAO);
        this.auditoriumService = auditoriumService;
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

    @Override
    @Transactional
    public void parseEventsFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            Set<EventDate> eventDateSet = new HashSet<>();
            for (int i = 3; i < split.length; i++) {
                LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(Long.parseLong(split[i]))).toLocalDateTime();
                Auditorium auditorium = auditoriumService.getByName(split[++i]);
                eventDateSet.add(new EventDate(dateTime, auditorium));
            }
            Event event = new Event();
            event.setName(split[0]);
            event.setBasePrice(Double.parseDouble(split[1]));
            event.setRating(EventRating.valueOf(split[2]));
            event.setAirDates(eventDateSet);
            save(event);
        }
        reader.close();
    }
}
