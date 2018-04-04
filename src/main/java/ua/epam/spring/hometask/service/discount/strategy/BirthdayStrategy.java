package ua.epam.spring.hometask.service.discount.strategy;

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
public class BirthdayStrategy implements DiscountStrategy {

    @Override
    public byte getDiscount(@Nullable User user, @Nonnull Event event,
                            @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        if (user == null || user.getBirthDate() == null) return 0;

        int days = Period.between(LocalDate.from(airDateTime), user.getBirthDate()).getDays();
        return (byte) (Math.abs(days) <= 5 ? 5 : 0);
    }
}
