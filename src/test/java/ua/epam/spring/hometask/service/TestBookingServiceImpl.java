package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
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
    private BookingService bookingService;
    private Event event;
    private User user;
    private Set<Ticket> tickets;

    @Before
    public void init() throws IOException {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 20);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("mail");
        user.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user);
        bookingService = new BookingServiceImpl(discountService, userService);

        AuditoriumService auditoriumService = new AuditoriumServiceImpl();

        TreeSet<EventDate> airDates = new TreeSet<>(Arrays.asList(
                new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 3, 10, 30), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditoriumService.getByName("1")),
                new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditoriumService.getByName("1"))
        ));

        event = new Event();
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(airDates);
        event = eventService.save(event);


        tickets = new HashSet<>();
        tickets.add(new Ticket(user, event,
                LocalDateTime.of(4018, 4, 3, 10, 30),
                1));
        tickets.add(new Ticket(user, event,
                LocalDateTime.of(4018, 4, 3, 10, 30),
                2));
    }

    @Test
    public void shouldReturn_115_2_OnGetTicketPriceCall() {
        double price = bookingService.getTicketsPrice(event,
                LocalDateTime.of(4018, 4, 3, 10, 30),
                null, new HashSet<>(Arrays.asList(1L,2L,3L)));
        assertEquals(115.2, price, 0.0000001);
    }

    @Test
    public void shouldReturn_153_6_OnGetTicketPriceCall() {
        double price = bookingService.getTicketsPrice(event,
                LocalDateTime.of(4018, 4, 3, 10, 30),
                null, new HashSet<>(Arrays.asList(1L,2L,5L)));
        assertEquals(153.6, price, 0.0000001);
    }

    @Test
    public void shouldAddTicketsToUserOnBookTicketsCall() {
        bookingService.bookTickets(tickets);

        assertEquals(2, userService.getUserByEmail(user.getEmail()).getTickets().size());
    }

    @Test
    public void shouldReturnPurchasedTicketsOnGetPurchasedTicketsForEventCall() {
        bookingService.bookTickets(tickets);
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event,
                LocalDateTime.of(4018, 4, 3, 10, 30));

        assertEquals(2, purchasedTicketsForEvent.size());
    }

    @Test
    public void shouldReturnEmptySetOnGetPurchasedTicketsForEventCall1() {
        bookingService.bookTickets(tickets);
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event,
                LocalDateTime.of(4018, 4, 2, 10, 30));

        assertEquals(0, purchasedTicketsForEvent.size());
    }
}
