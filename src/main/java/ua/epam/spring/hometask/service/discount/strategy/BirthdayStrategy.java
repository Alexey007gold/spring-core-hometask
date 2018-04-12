package ua.epam.spring.hometask.service.discount.strategy;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class BirthdayStrategy implements DiscountStrategy {

    @Override
    public Discount getDiscount(@Nullable User user, @Nonnull Event event,
                                @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        if (user == null || user.getBirthDate() == null) return null;

        int days = Period.between(LocalDate.from(airDateTime), user.getBirthDate()).getDays();
        return Math.abs(days) <= 5 ? new Discount("BirthdayStrategy", (byte) 5) : null;
    }
}
