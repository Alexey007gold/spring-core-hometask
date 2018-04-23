package ua.epam.spring.hometask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.interf.BookingFacadeService;
import ua.epam.spring.hometask.service.interf.BookingService;
import ua.epam.spring.hometask.service.interf.EventService;
import ua.epam.spring.hometask.service.interf.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TimeZone;
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
    private BookingFacadeService bookingFacadeService;

    public BookingController(BookingService bookingService, EventService eventService,
                             UserService userService, BookingFacadeService bookingFacadeService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.userService = userService;
        this.bookingFacadeService = bookingFacadeService;
    }

    @RequestMapping("/booked")
    public String getBookedTickets(@ModelAttribute("model") ModelMap model,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Long eventId,
                                   @RequestParam(required = false) Long time) {
        model.addAttribute("ticketList", getTicketsByUserIdAndEvent(userId, eventId, time));
        return "tickets";
    }

    @RequestMapping("/booked/pdf")
    public String getBookedTicketsPdf(Model model,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Long eventId,
                                   @RequestParam(required = false) Long time) {
        model.addAttribute("ticketList", getTicketsByUserIdAndEvent(userId, eventId, time));
        return "ticketPdfView";
    }

    @RequestMapping("/available")
    @ResponseBody
    public Set<Integer> getAvailableSeats(@RequestParam Long eventId, @RequestParam Long time) {
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        return bookingFacadeService.getAvailableSeats(eventId, dateTime);
    }

    @RequestMapping("/price")
    @ResponseBody
    public double getPrice(@RequestParam Long eventId, @RequestParam Long time,
                           @RequestParam(required = false) Long userId, @RequestParam String seats) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getDefault().toZoneId());

        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
        return bookingFacadeService.getTicketsPrice(eventId, dateTime, userId, seatsSet);
    }

    @RequestMapping("/book")
    @ResponseBody
    public Set<Integer> bookTickets(@RequestParam Long eventId, @RequestParam Long time,
                           @RequestParam(required = false) Long userId, @RequestParam String seats) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getDefault().toZoneId());

        Set<Integer> seatsSet = Arrays.stream(seats.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
        return bookingFacadeService.bookTickets(eventId, dateTime, userId, seatsSet);
    }


    private Set<Ticket> getTicketsByUserIdAndEvent(Long userId, Long eventId, Long time) {
        Set<Ticket> tickets;

        if (eventId != null) {
            Event event = eventService.getById(eventId);
            if (time != null) {
                tickets = bookingService.getPurchasedTicketsForEvent(event,
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getDefault().toZoneId()));
            } else {
                tickets = bookingService.getPurchasedTicketsForEvent(event);
            }

            if (userId != null) {
                tickets.removeIf(t -> !t.getUser().getId().equals(userId));
            }
        } else if (userId != null) {
            tickets = userService.getById(userId).getTickets();
        } else {
            tickets = Collections.emptySet();
        }
        return tickets;
    }
}
