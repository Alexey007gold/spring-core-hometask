package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface TicketDAO extends DomainObjectDAO<Ticket> {

    public List<Integer> getBookedSeatsForEventAndDate(Long eventId, LocalDateTime dateTime);
}
