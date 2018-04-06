package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestBookingServiceImpl {

    private BookingService bookingService;
    private Event event;
    private User user;
    private Set<Ticket> tickets;

    @Before
    public void init() throws IOException {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 20);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        UserServiceImpl userService = new UserServiceImpl();
        user = new User();
        userService.save(user);
        bookingService = new BookingServiceImpl(discountService, userService);


        TreeSet<LocalDateTime> airDates = new TreeSet<>(Arrays.asList(
                LocalDateTime.of(4018, 4, 2, 10, 30),
                LocalDateTime.of(4018, 4, 2, 18, 0),
                LocalDateTime.of(4018, 4, 3, 10, 30),
                LocalDateTime.of(4018, 4, 4, 10, 20),
                LocalDateTime.of(4018, 4, 5, 10, 30)
        ));

        AuditoriumService auditoriumService = new AuditoriumServiceImpl();

        Map<LocalDateTime, Auditorium> auditoriumMap = airDates.stream().collect(toMap(e -> e, e -> auditoriumService.getByName("1")));

        event = new Event();
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(airDates);
        event.setAuditoriums(new TreeMap<>(auditoriumMap));


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

        assertEquals(2, user.getTickets().size());
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
