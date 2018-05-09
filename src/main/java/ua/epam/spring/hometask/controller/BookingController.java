package ua.epam.spring.hometask.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.interf.BookingFacadeService;
import ua.epam.spring.hometask.service.interf.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.epam.spring.hometask.view.TicketPdfView.TICKET_LIST;

/**
 * Created by Oleksii_Kovetskyi on 4/15/2018.
 */
@Controller
@RequestMapping("/tickets")
public class BookingController {

    private UserService userService;
    private BookingFacadeService bookingFacadeService;

    public BookingController(UserService userService, BookingFacadeService bookingFacadeService) {
        this.userService = userService;
        this.bookingFacadeService = bookingFacadeService;
    }

    @RequestMapping("/booked")
    public String getBookedTickets(Model model,
                                  Authentication authentication,
                                  @RequestParam(required = false) Long eventId,
                                  @RequestParam(required = false) Long time,
                                  @RequestParam(required = false, defaultValue = "true") boolean onlyMyTickets,
                                  @RequestParam(required = false, defaultValue = "false") boolean pdf) {
        Set<Ticket> ticketsByUserIdAndEvent =
                bookingFacadeService.getTickets(authentication.getName(), eventId, time, onlyMyTickets);

        model.addAttribute(TICKET_LIST, ticketsByUserIdAndEvent);
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
}
