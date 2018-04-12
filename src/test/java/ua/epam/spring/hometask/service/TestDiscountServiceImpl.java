package ua.epam.spring.hometask.service;

import org.junit.Test;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestDiscountServiceImpl {

    @Test
    public void shouldReturn_8_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("a", (byte) 5));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("b", (byte) 8));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("c", (byte) 4));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("d", (byte) 1));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("e", (byte) 6));
        DiscountService discountService = new DiscountServiceImpl(strategies);

        Discount discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 0L);
        assertEquals(8, discount.getPercent());
        assertEquals("b", discount.getDiscountType());
    }

    @Test
    public void shouldReturn_14_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("a", (byte) 5));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("b", (byte) 8));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("c", (byte) 4));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("d", (byte) 14));
        strategies.add((user, event, airDateTime, numberOfTickets) -> new Discount("e", (byte) 6));
        DiscountService discountService = new DiscountServiceImpl(strategies);

        Discount discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 0L);
        assertEquals(14, discount.getPercent());
        assertEquals("d", discount.getDiscountType());
    }
}
