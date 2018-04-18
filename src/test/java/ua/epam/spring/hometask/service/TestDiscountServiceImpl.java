package ua.epam.spring.hometask.service;

import org.junit.Test;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestDiscountServiceImpl {

    @Test
    public void shouldReturn_8_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("a", (byte) 5)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("b", (byte) 8)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("c", (byte) 4)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("d", (byte) 1)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("e", (byte) 6)));
        DiscountService discountService = new DiscountServiceImpl(strategies);

        List<Discount> discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 1L);
        assertEquals(1, discount.size());
        assertEquals(8, discount.get(0).getPercent());
        assertEquals("b", discount.get(0).getDiscountType());
    }

    @Test
    public void shouldReturn_14_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("a", (byte) 5)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("b", (byte) 8)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("c", (byte) 4)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("d", (byte) 14)));
        strategies.add((user, event, airDateTime, numberOfTickets) -> Collections.singletonList(new Discount("e", (byte) 6)));
        DiscountService discountService = new DiscountServiceImpl(strategies);

        List<Discount> discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 1L);
        assertEquals(1, discount.size());
        assertEquals(14, discount.get(0).getPercent());
        assertEquals("d", discount.get(0).getDiscountType());
    }
}
