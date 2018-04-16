package ua.epam.spring.hometask.aspect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestCounterAspect {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventCounterStatsService eventCounterStatsService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscountService discountService;

    private User user;

    @Before
    public void init() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("example@gmail.com");
        user.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user);

        Auditorium auditorium = new Auditorium();
        auditorium.setName("1");
        TreeSet<EventDate> airDates1 = new TreeSet<EventDate>(){{
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditorium));
        }};

        Event event1 = new Event();
        event1.setName("Titanik");
        event1.setBasePrice(40);
        event1.setRating(HIGH);
        event1.setAirDates(airDates1);
        eventService.save(event1);
    }

    @Test
    public void accessByNameCountShouldBeOne() {
        Event event = eventService.getByName("Titanik");
        assertEquals(1, eventCounterStatsService.getAccessByNameCount(event));
        assertEquals(0, eventCounterStatsService.getPriceQueryCount(event));
        assertEquals(0, eventCounterStatsService.getTicketsBookedTimesCount(event));
    }

    @Test
    public void priceQueryCountShouldBeOne() {
        Event event = eventService.getByName("Titanik");
        LocalDateTime dateTime = LocalDateTime.of(4018, 4, 2, 18, 0);
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1L, 3L, 4L)));
        bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets);

        assertEquals(1, eventCounterStatsService.getAccessByNameCount(event));
        assertEquals(1, eventCounterStatsService.getPriceQueryCount(event));
        assertEquals(0, eventCounterStatsService.getTicketsBookedTimesCount(event));
    }

    @Test
    public void bookedTicketsCountShouldBeTwo() {
        Event event = eventService.getByName("Titanik");
        LocalDateTime dateTime = LocalDateTime.of(4018, 4, 2, 10, 30);

        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1L, 2L)));

        List<Discount> discount = discountService.getDiscount(user, event, dateTime, 2);
        bookingService.bookTickets(tickets, discount);

        assertEquals(1, eventCounterStatsService.getAccessByNameCount(event));
        assertEquals(0, eventCounterStatsService.getPriceQueryCount(event));
        assertEquals(2, eventCounterStatsService.getTicketsBookedTimesCount(event));
    }
}
