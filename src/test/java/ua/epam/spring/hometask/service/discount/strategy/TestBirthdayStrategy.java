package ua.epam.spring.hometask.service.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;
import ua.epam.spring.hometask.service.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestBirthdayStrategy {

    private DiscountStrategy discountStrategy;
    private UserService userService;
    private Event event;

    @Before
    public void init() {
        userService = new UserServiceImpl();


        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user1);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("exampleJane@gmail.com");
        user2.setBirthDate(LocalDate.of(1993, 4, 4));
        userService.save(user2);

        TreeSet<LocalDateTime> airDates1 = new TreeSet<>(Arrays.asList(
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
        event.setAirDates(airDates1);

        discountStrategy = new BirthdayStrategy();
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCall() {
        User user = userService.getById(1L);

        byte discount = discountStrategy.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        assertEquals(0, discount);
    }

    @Test
    public void shouldReturn_5O_nGetDiscountCall() {
        User user = userService.getById(2L);

        byte discount = discountStrategy.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 9);

        assertEquals(5, discount);
    }
}
