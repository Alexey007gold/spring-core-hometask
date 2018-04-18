package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.interf.DiscountService;
import ua.epam.spring.hometask.service.interf.EventService;
import ua.epam.spring.hometask.service.interf.UserService;
import ua.epam.spring.hometask.util.Utils;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;
import static ua.epam.spring.hometask.domain.EventRating.MID;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestEvery10TicketStrategy {
    @Autowired
    private DiscountService discountService;
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
        user1.setFirstName("John");
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
    public void shouldReturn_0_OnGetDiscountCallForUser1() {
        List<Discount> discount = discountService.getDiscount(user1, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 2);

        for (int i = 0; i < discount.size(); i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUser1() {
        List<Discount> discount = discountService.getDiscount(user1, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 3);

        for (int i = 0; i < 2; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(2).getPercent());
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUser2() {
        List<Discount> discount = discountService.getDiscount(user2, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        for (int i = 0; i < discount.size(); i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
    }

    @Test
    public void shouldReturn_5_OnGetDiscountCallForUser2() {
        List<Discount> discount = discountService.getDiscount(user2, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(9).getPercent());
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUnknownUser() {
        User user = new User();

        List<Discount> discount1 = discountService.getDiscount(null, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);
        List<Discount> discount2 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 4);
        user.setId(5L);
        List<Discount> discount3 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        for (Discount discount : discount1) {
            assertEquals(0, discount.getPercent());
        }
        for (Discount discount : discount2) {
            assertEquals(0, discount.getPercent());
        }
        for (Discount discount : discount3) {
            assertEquals(0, discount.getPercent());
        }
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUnknownUser1() {
        List<Discount> discount = discountService.getDiscount(null, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(9).getPercent());
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUnknownUser2() {
        User user = new User();

        List<Discount> discount = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 25);
        user.setId(5L);

        for (int i = 0; i < discount.size(); i++) {
            if (i != 9 && i != 19) {
                assertEquals(0, discount.get(i).getPercent());
            } else {
                assertEquals(50, discount.get(i).getPercent());
            }
        }
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUnknownUser3() {
        User user = new User();
        user.setId(5L);

        List<Discount> discount = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        for (int i = 0; i < discount.size(); i++) {
            if (i != 9) {
                assertEquals(0, discount.get(i).getPercent());
            } else {
                assertEquals(50, discount.get(i).getPercent());
            }
        }
    }
}
