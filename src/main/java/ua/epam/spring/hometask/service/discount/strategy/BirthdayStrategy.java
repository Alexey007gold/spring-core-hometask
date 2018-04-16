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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class BirthdayStrategy implements DiscountStrategy {

    @Override
    public List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                                      @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        List<Discount> discountList = new ArrayList<>((int)numberOfTickets);
        Discount discount = new Discount("", (byte) 0);
        if (user != null && user.getBirthDate() != null) {
            int days = Period.between(LocalDate.from(airDateTime), user.getBirthDate()).getDays();
            if (Math.abs(days) <= 5) {
                discount = new Discount("BirthdayStrategy", (byte) 5);
            }
        }
        for (long i = 0; i < numberOfTickets; i++) {
            discountList.add(discount);
        }
        return discountList;
    }
}
