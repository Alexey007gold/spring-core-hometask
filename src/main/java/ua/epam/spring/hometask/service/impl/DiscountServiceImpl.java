package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;
import ua.epam.spring.hometask.service.interf.DiscountService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    private List<DiscountStrategy> discountStrategies;

    public DiscountServiceImpl(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    @Override
    public List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                                      @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        List<Discount> discountList = new ArrayList<>((int) numberOfTickets);
        Discount disc = new Discount();
        for (long i = 0; i < numberOfTickets; i++) {
            discountList.add(disc);
        }
        for (DiscountStrategy discountStrategy : discountStrategies) {
            List<Discount> discounts = discountStrategy.getDiscount(user, event, airDateTime, numberOfTickets);
            for (int i = 0; i < numberOfTickets; i++) {
                if (discountList.get(i).compareTo(discounts.get(i)) < 0) {
                    discountList.set(i, discounts.get(i));
                }
            }
        }
        return discountList;
    }
}
