package ua.epam.spring.hometask.service.interf;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/23/2018.
 */
public interface BookingFacadeService {
    double getTicketsPrice(Long eventId, LocalDateTime dateTime, Long userId, Set<Integer> seats);

    Set<Integer> getAvailableSeats(Long eventId, LocalDateTime dateTime);

    Set<Integer> bookTickets(Long eventId, LocalDateTime dateTime, Long userId, Set<Integer> seats);

    void refillAccount(Long userId, double sum);
}
