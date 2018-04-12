package ua.epam.spring.hometask.service;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public Discount getDiscount(@Nullable User user, @Nonnull Event event,
                                @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        return discountStrategies.stream()
                .map(s -> s.getDiscount(user, event, airDateTime, numberOfTickets))
                .filter(Objects::nonNull)
                .max(Discount::compareTo).orElse(null);
    }
}
