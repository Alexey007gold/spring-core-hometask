package ua.epam.spring.hometask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.BookingService;
import ua.epam.spring.hometask.service.interf.DiscountService;
import ua.epam.spring.hometask.service.interf.EventService;
import ua.epam.spring.hometask.service.interf.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/15/2018.
 */
@Controller
@RequestMapping("/tickets")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscountService discountService;

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
    public Set<Long> getAvailableSeats(@RequestParam Long eventId, @RequestParam Long time) {
        Event event = eventService.getById(eventId);
        LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(time)).toLocalDateTime();
        Set<Ticket> bookedTickets = bookingService.getPurchasedTicketsForEvent(event, dateTime);

        long numberOfSeats = event.getAirDates().get(dateTime).getAuditorium().getNumberOfSeats();
        Set<Long> availableSeats = new HashSet<>();
        for (long i = 1; i <= numberOfSeats; i++) {
            availableSeats.add(i);
        }
        for (Ticket bookedTicket : bookedTickets) {
            availableSeats.remove(bookedTicket.getSeat());
        }
        return availableSeats;
    }

    @RequestMapping("/price")
    @ResponseBody
    public double getPrice(@RequestParam Long eventId, @RequestParam Long time,
                           @RequestParam(required = false) Long userId, @RequestParam String seats) {
        Event event = eventService.getById(eventId);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getDefault().toZoneId());
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        Set<Long> seatsSet = Arrays.stream(seats.split(",")).map(Long::parseLong).collect(Collectors.toSet());
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, seatsSet);
        return bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets);
    }

    @RequestMapping("/book")
    @ResponseBody
    public Set<Long> bookTickets(@RequestParam Long eventId, @RequestParam Long time,
                           @RequestParam(required = false) Long userId, @RequestParam String seats) {
        Event event = eventService.getById(eventId);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getDefault().toZoneId());
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }

        Set<Long> seatsSet = Arrays.stream(seats.split(",")).map(Long::parseLong).collect(Collectors.toSet());
        List<Ticket> ticketList = bookingService.generateTickets(event, dateTime, user, seatsSet);

        List<Discount> discountList = discountService.getDiscount(user, event, dateTime, seats.length());
        return bookingService.bookTickets(ticketList, discountList);
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
