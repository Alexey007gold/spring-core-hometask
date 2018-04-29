package ua.epam.spring.hometask.service.impl;

import org.junit.Test;
import ua.epam.spring.hometask.TestDataCreator;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;
import ua.epam.spring.hometask.service.interf.DiscountService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestDiscountServiceImpl {

    @Test
    public void shouldReturn_8_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = TestDataCreator.createMockDiscountStrategyList(5, 8, 4, 1, 6);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        List<Discount> discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 1);
        assertEquals(1, discount.size());
        assertEquals(8, discount.get(0).getPercent());
        assertEquals("b", discount.get(0).getDiscountType());
    }

    @Test
    public void shouldReturn_14_OnGetDiscountCall() {
        List<DiscountStrategy> strategies = TestDataCreator.createMockDiscountStrategyList(5, 8, 4, 14, 6);
        DiscountService discountService = new DiscountServiceImpl(strategies);

        List<Discount> discount = discountService.getDiscount(null, new Event(), LocalDateTime.now(), 1);
        assertEquals(1, discount.size());
        assertEquals(14, discount.get(0).getPercent());
        assertEquals("d", discount.get(0).getDiscountType());
    }
}
