package ua.epam.spring.hometask.controller.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.epam.spring.hometask.service.interf.BookingFacadeService;
import ua.epam.spring.hometask.service.interf.UserService;
import ua.epam.spring.hometask.view.messageconverter.TicketList;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
@RestController
@RequestMapping("/rest/tickets")
public class BookingRestController {

    private UserService userService;
    private BookingFacadeService bookingFacadeService;

    public BookingRestController(UserService userService, BookingFacadeService bookingFacadeService) {
        this.userService = userService;
        this.bookingFacadeService = bookingFacadeService;
    }

    @RequestMapping("/booked")
    public TicketList getBookedTickets(Authentication authentication,
                                   @RequestParam(required = false) Long eventId,
                                   @RequestParam(required = false) Long time,
                                   @RequestParam(required = false, defaultValue = "true") boolean onlyMyTickets) {
        return new TicketList(bookingFacadeService.getTickets(authentication.getName(), eventId, time, onlyMyTickets));
    }

    @RequestMapping("/available")
    public Set<Integer> getAvailableSeats(@RequestParam Long eventId, @RequestParam Long time) {
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        return bookingFacadeService.getAvailableSeats(eventId, dateTime);
    }

    @RequestMapping("/price")
    public double getPrice(Authentication authentication, @RequestParam Long eventId,
                           @RequestParam Long time, @RequestParam String seats) {
        Long userId = userService.getUserIdByLogin(authentication.getName());
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());

        return bookingFacadeService.getTicketsPrice(eventId, dateTime, userId, seatsSet);
    }

    @RequestMapping("/book")
    public Set<Integer> bookTickets(Authentication authentication, @RequestParam Long eventId,
                                    @RequestParam Long time, @RequestParam String seats) {
        Long userId = userService.getUserIdByLogin(authentication.getName());
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());

        return bookingFacadeService.bookTickets(eventId, dateTime, userId, seatsSet);
    }
}
