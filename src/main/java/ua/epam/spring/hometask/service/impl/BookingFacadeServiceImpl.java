package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.interf.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/23/2018.
 */
@Service
public class BookingFacadeServiceImpl implements BookingFacadeService {

    private BookingService bookingService;
    private EventService eventService;
    private UserService userService;
    private UserAccountService userAccountService;
    private DiscountService discountService;
    private TicketService ticketService;

    public BookingFacadeServiceImpl(BookingService bookingService, EventService eventService,
                                    UserService userService, UserAccountService userAccountService,
                                    DiscountService discountService, TicketService ticketService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.userService = userService;
        this.userAccountService = userAccountService;
        this.discountService = discountService;
        this.ticketService = ticketService;
    }

    @Override
    public double getTicketsPrice(Long eventId, LocalDateTime dateTime, Long userId, Set<Integer> seats) {

        Event event = eventService.getById(eventId);
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, seats);
        return bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets);
    }

    @Override
    public Set<Integer> getAvailableSeats(Long eventId, LocalDateTime dateTime) {
        Event event = eventService.getById(eventId);
        if (event == null)
            throw new IllegalArgumentException("No such event!");
        Set<Ticket> bookedTickets = new HashSet<>(ticketService.getByEventAndTime(event, dateTime));

        int numberOfSeats = event.getAirDates().get(dateTime).getAuditorium().getNumberOfSeats();
        Set<Integer> availableSeats = new HashSet<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            availableSeats.add(i);
        }
        for (Ticket bookedTicket : bookedTickets) {
            availableSeats.remove(bookedTicket.getSeat());
        }
        return availableSeats;
    }

    @Override
    public Set<Integer> bookTickets(Long eventId, LocalDateTime dateTime, Long userId, Set<Integer> seats) {
        Event event = eventService.getById(eventId);
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }

        List<Ticket> ticketList = bookingService.generateTickets(event, dateTime, user, seats);

        List<Discount> discountList = discountService.getDiscount(user, event, dateTime, seats.size());
        return bookingService.bookTickets(ticketList, discountList);
    }

    @Override
    public void refillAccount(Long userId, double sum) {
        UserAccount userAccount = userAccountService.getByUserId(userId);
        if (userAccount != null) {
            userAccount.setBalance(userAccount.getBalance() + sum);
            userAccountService.save(userAccount);
        }
    }
}
