package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestBirthdayStrategy {

    private LocalDateTime dateTime = LocalDateTime.of(4018, 4, 4, 10, 20);
    private DiscountStrategy discountStrategy;

    private User user1;
    private User user2;

    @Before
    public void init() {
        user1 = new User();
        user1.setBirthDate(LocalDate.of(1990, 11, 11));

        user2 = new User();
        user2.setBirthDate(LocalDate.of(1993, 4, 4));

        discountStrategy = new BirthdayStrategy();
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCall() {
        List<Discount> discount = discountStrategy.getDiscount(user1, new Event(),
                dateTime, 9);

        for (Discount d : discount) {
            assertEquals(0, d.getPercent());
        }
    }

    @Test
    public void shouldReturn_5_OnGetDiscountCall() {
        List<Discount> discount = discountStrategy.getDiscount(user2, new Event(),
                dateTime, 9);

        for (Discount d : discount) {
            assertEquals(5, d.getPercent());
        }
    }
}
