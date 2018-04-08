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
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventCounterStatsService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
        bookingService.getTicketsPrice(event,
                LocalDateTime.of(4018, 4, 2, 18, 0),
                user, new HashSet<>(Arrays.asList(1L, 3L, 4L)));

        assertEquals(1, eventCounterStatsService.getAccessByNameCount(event));
        assertEquals(1, eventCounterStatsService.getPriceQueryCount(event));
        assertEquals(0, eventCounterStatsService.getTicketsBookedTimesCount(event));
    }

    @Test
    public void bookedTicketsCountShouldBeTwo() {
        Event event = eventService.getByName("Titanik");

        Set<Ticket> tickets = new HashSet<>();
        tickets.add(new Ticket(user, event,
                LocalDateTime.of(4018, 4, 2, 10, 30),
                1, 40));
        tickets.add(new Ticket(user, event,
                LocalDateTime.of(4018, 4, 2, 18, 0),
                2, 40));

        bookingService.bookTickets(tickets);

        assertEquals(1, eventCounterStatsService.getAccessByNameCount(event));
        assertEquals(0, eventCounterStatsService.getPriceQueryCount(event));
        assertEquals(2, eventCounterStatsService.getTicketsBookedTimesCount(event));
    }
}
