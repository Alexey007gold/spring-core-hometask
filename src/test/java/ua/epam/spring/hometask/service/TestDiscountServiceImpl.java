package ua.epam.spring.hometask.service;

import org.junit.Test;
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
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 5);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 8);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 4);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 1);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 6);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        byte discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 0L);
        assertEquals(8, discount);
    }

    @Test
    public void shouldReturn_14_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 5);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 8);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 4);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 14);
        strategies.add((user, event, airDateTime, numberOfTickets) -> (byte) 6);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        byte discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 0L);
        assertEquals(14, discount);
    }
}
