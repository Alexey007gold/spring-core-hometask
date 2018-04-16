package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestBookingServiceImpl {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;

    private BookingService bookingService;
    @Autowired
    private DiscountService discountService;
    private Event event;
    private User user;
    private List<Ticket> tickets;
    private LocalDateTime dateTime;

    @Before
    public void init() throws IOException {
        List<DiscountStrategy> strategies = new ArrayList<>();
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount, discount);
        strategies.add((user, event, airDateTime, numberOfTickets) -> discountList);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("mail");
        user.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user);
        bookingService = new BookingServiceImpl(discountService, userService, ticketService);

        AuditoriumService auditoriumService = new AuditoriumServiceImpl();

        dateTime = LocalDateTime.of(4018, 4, 3, 10, 30);
        TreeSet<EventDate> airDates = new TreeSet<>(Arrays.asList(
                new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditoriumService.getByName("1")),
                new EventDate(dateTime, auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditoriumService.getByName("1"))
        ));

        event = new Event();
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(airDates);
        event = eventService.save(event);


        tickets = new ArrayList<>();
        tickets.add(new Ticket(user, event, dateTime, 1, 40));
        tickets.add(new Ticket(user, event, dateTime, 2, 40));
    }

    @Test
    public void shouldReturn_115_2_OnGetTicketPriceCall() {
        LocalDateTime dateTime = LocalDateTime.of(4018, 4, 3, 10, 30);
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1L, 2L,3L)));
        double price = bookingService.getTicketsPriceWithDiscount(event, dateTime, null, tickets);
        assertEquals(115.2, price, 0.0000001);
    }

    @Test
    public void shouldReturn_153_6_OnGetTicketPriceCall() {
        LocalDateTime dateTime = LocalDateTime.of(4018, 4, 3, 10, 30);
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1L, 2L,5L)));
        double price = bookingService.getTicketsPriceWithDiscount(event, dateTime, null, tickets);
        assertEquals(153.6, price, 0.0000001);
    }

    @Test
    public void shouldAddTicketsToUserOnBookTicketsCall() {
        List<Discount> discount = discountService.getDiscount(user, event, dateTime, 2);
        bookingService.bookTickets(tickets, discount);

        assertEquals(2, userService.getUserByEmail(user.getEmail()).getTickets().size());
    }

    @Test
    public void shouldReturnPurchasedTicketsOnGetPurchasedTicketsForEventCall() {
        List<Discount> discount = discountService.getDiscount(user, event, dateTime, 2);
        bookingService.bookTickets(tickets, discount);
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event,
                LocalDateTime.of(4018, 4, 3, 10, 30));

        assertEquals(2, purchasedTicketsForEvent.size());
    }

    @Test
    public void shouldReturnEmptySetOnGetPurchasedTicketsForEventCall1() {
        List<Discount> discount = discountService.getDiscount(user, event, dateTime, 2);
        bookingService.bookTickets(tickets, discount);
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event,
                LocalDateTime.of(4018, 4, 2, 10, 30));

        assertEquals(0, purchasedTicketsForEvent.size());
    }
}
