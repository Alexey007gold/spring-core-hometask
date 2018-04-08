package ua.epam.spring.hometask.aspect;

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
import ua.epam.spring.hometask.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestLuckyWinnerAspect {

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;

    private Event event;
    private User user;

    @Before
    public void init() throws IOException {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("mail");
        user.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user);

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
    }


    @Test
    public void ticketPriceShouldBeZeroAfterBookTicketCall() {
        Ticket ticket = new Ticket(user, event,
                LocalDateTime.of(4018, 4, 3, 10, 30),
                1, 40);

        LuckyWinnerAspect.bound = 1;
        bookingService.bookTicket(ticket);
        assertEquals(0, ticket.getPrice(), 0);
    }
}
