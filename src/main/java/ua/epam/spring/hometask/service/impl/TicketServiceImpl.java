package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.interf.TicketDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.TicketService;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Service
public class TicketServiceImpl extends AbstractDomainObjectServiceImpl<Ticket, TicketDAO> implements TicketService {

    public TicketServiceImpl(TicketDAO domainObjectDAO) {
        super(domainObjectDAO);
    }

    @Override
    public Collection<Ticket> getByUser(@Nonnull User user) {
        return getByUserId(user.getId());
    }

    @Override
    public Collection<Ticket> getByUserId(@Nonnull Long userId) {
        return domainObjectDAO.getBy(new String[] {"user_id"}, userId);
    }

    @Override
    public Collection<Ticket> getByEvent(@Nonnull Event event) {
        return getByEventId(event.getId());
    }

    @Override
    public Collection<Ticket> getByEventId(@Nonnull Long eventId) {
        return domainObjectDAO.getBy(new String[] {"event_id"}, eventId);
    }

    @Override
    public Collection<Ticket> getByEventAndTime(Event event, LocalDateTime dateTime) {
        return domainObjectDAO.getBy(new String[] {"event_id", "time"}, event.getId(), Timestamp.valueOf(dateTime));
    }

    @Override
    public Collection<Ticket> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to) {
        return domainObjectDAO.getAll().stream()
                .filter(t -> (LocalDate.from(t.getDateTime()).isAfter(from) || LocalDate.from(t.getDateTime()).isEqual(from)) &&
                (LocalDate.from(t.getDateTime()).isBefore(to) || LocalDate.from(t.getDateTime()).isEqual(to))).collect(Collectors.toSet());
    }
}
