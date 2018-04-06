package ua.epam.spring.hometask.service;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
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
    public byte getDiscount(@Nullable User user, @Nonnull Event event,
                            @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        return (byte) discountStrategies.stream()
                .mapToInt(s -> s.getDiscount(user, event, airDateTime, numberOfTickets))
                .max().orElse(0);
    }
}
