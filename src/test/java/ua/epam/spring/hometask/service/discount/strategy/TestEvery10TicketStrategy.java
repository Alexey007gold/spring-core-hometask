package ua.epam.spring.hometask.service.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.DiscountServiceImpl;
import ua.epam.spring.hometask.service.UserService;
import ua.epam.spring.hometask.service.UserServiceImpl;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestEvery10TicketStrategy {

    private DiscountService discountService;
    private UserService userService;
    private Event event;

    @Before
    public void init() {
        userService = new UserServiceImpl();

        NavigableSet<Ticket> tickets = new TreeSet<>();
        for (int i = 0; i < 7; i++) {
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            String eventName = new String(array, Charset.forName("UTF-8"));
            Event event = new Event();
            event.setName(eventName);
            tickets.add(new Ticket(new User(), event, LocalDateTime.now(), 0));
        }
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setTickets(tickets);
        userService.save(user1);

        User user2 = new User();
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
        event = new Event();
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(airDates);

        List<DiscountStrategy> discountStrategies =
                Arrays.asList(new BirthdayStrategy(), new Every10TicketStrategy(userService));
        discountService = new DiscountServiceImpl(discountStrategies);
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUser1() {
        User user1 = userService.getById(1L);

        byte discount = discountService.getDiscount(user1, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 2);

        assertEquals(0, discount);
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUser1() {
        User user1 = userService.getById(1L);

        byte discount = discountService.getDiscount(user1, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 3);

        assertEquals(50, discount);
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUser2() {
        User user2 = userService.getById(2L);

        byte discount = discountService.getDiscount(user2, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        assertEquals(0, discount);
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUser2() {
        User user2 = userService.getById(2L);

        byte discount = discountService.getDiscount(user2, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        assertEquals(50, discount);
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUnknownUser() {
        User user = new User();

        byte discount1 = discountService.getDiscount(null, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);
        byte discount2 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 4);
        user.setId(5L);
        byte discount3 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        assertEquals(0, discount1);
        assertEquals(0, discount2);
        assertEquals(0, discount3);
    }

    @Test
    public void shouldReturn_50_OnGetDiscountCallForUnknownUser() {
        User user = new User();

        byte discount1 = discountService.getDiscount(null, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);
        byte discount2 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 25);
        user.setId(5L);
        byte discount3 = discountService.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        assertEquals(50, discount1);
        assertEquals(50, discount2);
        assertEquals(50, discount3);
    }
}
