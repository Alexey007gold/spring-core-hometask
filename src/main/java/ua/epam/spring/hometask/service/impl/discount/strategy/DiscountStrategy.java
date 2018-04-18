package ua.epam.spring.hometask.service.impl.discount.strategy;

import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public interface DiscountStrategy {

    List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                               @Nonnull LocalDateTime airDateTime, long numberOfTickets);
}
