package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public interface TicketService extends AbstractDomainObjectService<Ticket> {

    Collection<Ticket> getByUser(@Nonnull User user);

    Collection<Ticket> getByUserId(@Nonnull Long userId);

    Collection<Ticket> getByEvent(@Nonnull Event event);

    Collection<Ticket> getByEventId(@Nonnull Long eventId);

    Collection<Ticket> getByEventAndTime(Event event, LocalDateTime dateTime);

    Collection<Ticket> getByUserIdAndEventId(Long userId, Long eventId);

    Collection<Ticket> getByUserIdAndEventIdAndDateTime(Long userId, Long eventId, LocalDateTime dateTime);

    Collection<Ticket> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to);

    List<Integer> getBookedSeatsForEventAndDate(Long eventId, LocalDateTime dateTime);
}
