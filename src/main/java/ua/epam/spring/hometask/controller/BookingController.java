package ua.epam.spring.hometask.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.interf.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/15/2018.
 */
@Controller
@RequestMapping("/tickets")
public class BookingController {

    private BookingService bookingService;
    private EventService eventService;
    private UserService userService;
    private TicketService ticketService;
    private BookingFacadeService bookingFacadeService;

    public BookingController(BookingService bookingService, EventService eventService,
                             UserService userService, TicketService ticketService,
                             BookingFacadeService bookingFacadeService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.userService = userService;
        this.ticketService = ticketService;
        this.bookingFacadeService = bookingFacadeService;
    }

    @RequestMapping("/booked")
    public String getBookedTickets(Model model,
                                  Authentication authentication,
                                  @RequestParam(required = false) Long eventId,
                                  @RequestParam(required = false) Long time,
                                  @RequestParam(required = false, defaultValue = "true") boolean onlyMyTickets,
                                  @RequestParam(required = false, defaultValue = "false") boolean pdf) {
        Set<Ticket> ticketsByUserIdAndEvent = getTickets(authentication.getName(), eventId, time, onlyMyTickets);

        model.addAttribute("ticketList", ticketsByUserIdAndEvent);
        return pdf ? "ticketPdfView" : "tickets";
    }

    @RequestMapping("/available")
    @ResponseBody
    public Set<Integer> getAvailableSeats(@RequestParam Long eventId, @RequestParam Long time) {
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        return bookingFacadeService.getAvailableSeats(eventId, dateTime);
    }

    @RequestMapping("/price")
    @ResponseBody
    public double getPrice(Authentication authentication, @RequestParam Long eventId,
                           @RequestParam Long time, @RequestParam String seats) {
        Long userId = userService.getUserIdByLogin(authentication.getName());
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());

        return bookingFacadeService.getTicketsPrice(eventId, dateTime, userId, seatsSet);
    }

    @RequestMapping("/book")
    @ResponseBody
    public Set<Integer> bookTickets(Authentication authentication, @RequestParam Long eventId,
                                    @RequestParam Long time, @RequestParam String seats) {
        Long userId = userService.getUserIdByLogin(authentication.getName());
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());

        return bookingFacadeService.bookTickets(eventId, dateTime, userId, seatsSet);
    }


    private Set<Ticket> getTickets(String userLogin, Long eventId, Long time, boolean onlyMyTickets) {
        Long userId = userService.getUserIdByLogin(userLogin);
        LocalDateTime dateTime = null;
        if (time != null) {
            dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        }

        if (onlyMyTickets) {
            if (eventId != null) {
                if (dateTime != null) {
                    return new HashSet<>(ticketService.getByUserIdAndEventIdAndDateTime(userId, eventId, dateTime));
                } else {
                    return new HashSet<>(ticketService.getByUserIdAndEventId(userId, eventId));
                }
            } else {
                if (userId == null)
                    throw new IllegalStateException("Authenticated user is not found");
                return new HashSet<>(ticketService.getByUserId(userId));
            }
        } else if (eventId != null) {
            if (dateTime != null) {
                return bookingService.getPurchasedTicketsForEvent(eventService.getById(eventId), dateTime);
            } else {
                return bookingService.getPurchasedTicketsForEvent(eventService.getById(eventId));
            }
        } else {
            return Collections.emptySet();
        }
    }
}
