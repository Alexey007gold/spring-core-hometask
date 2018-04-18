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
import ua.epam.spring.hometask.service.interf.*;
import ua.epam.spring.hometask.util.Utils;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;
import static ua.epam.spring.hometask.domain.EventRating.MID;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestDiscountAspect {

    @Autowired
    private DiscountCounterService discountCounterService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    private Event event;

    private User user1;
    private User user2;

    @Before
    public void init() {
        user1 = new User();
        user1.setFirstName("Johna");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");

        NavigableSet<Ticket> tickets = new TreeSet<>();
        for (int i = 0; i < 7; i++) {
            Event event = new Event();
            event.setName(Utils.randomStr());
            event.setRating(MID);
            eventService.save(event);
            tickets.add(new Ticket(user1, event, LocalDateTime.now(), 0, 40));
        }
        user1.setTickets(tickets);
        userService.save(user1);

        user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("exampleJane@gmail.com");
        userService.save(user2);

        TreeSet<LocalDateTime> airDates = new TreeSet<>(Arrays.asList(
                LocalDateTime.of(4018, 4, 2, 10, 30),
                LocalDateTime.of(4018, 4, 2, 18, 0),
                LocalDateTime.of(4018, 4, 3, 10, 30),
                LocalDateTime.of(4018, 4, 4, 10, 20),
                LocalDateTime.of(4018, 4, 5, 10, 30)
        ));
        Auditorium auditorium = new Auditorium();
        auditorium.setName("1");
        Set<EventDate> auditoriumMap = airDates.stream().map(e -> new EventDate(e, auditorium)).collect(toSet());

        event = new Event();
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(auditoriumMap);
        event = eventService.save(event);
    }

    @Test
    public void discountStatTimesForUserShouldBe_1_afterGetDiscountCall() {
        LocalDateTime dateTime = LocalDateTime.of(4018, 4, 4, 10, 20);
        List<Discount> discount = discountService.getDiscount(user1, event, dateTime, 3);
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user1, new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        bookingService.bookTickets(tickets, discount);

        Collection<DiscountStats> discountStatsForUser = discountCounterService.getDiscountStatsForUser(user1);
        assertEquals(1, discountStatsForUser.size());
        assertEquals("Every10TicketStrategy", discountStatsForUser.stream().findFirst().get().getDiscountType());
        assertEquals(1, (long) discountStatsForUser.stream().findFirst().get().getTimes());
    }
}
