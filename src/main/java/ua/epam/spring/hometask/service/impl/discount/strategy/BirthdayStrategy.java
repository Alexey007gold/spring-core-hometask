package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class BirthdayStrategy implements DiscountStrategy {

    private static final String STRATEGY_NAME = "BirthdayStrategy";
    private byte percent;

    public BirthdayStrategy() throws IOException {
        init();
    }

    public void init() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));

        percent = Byte.parseByte(props.getProperty("discount.birthday"));
    }

    @Override
    public List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                                      @Nonnull LocalDateTime airDateTime, int numberOfTickets) {
        List<Discount> discountList = new ArrayList<>(numberOfTickets);
        Discount discount = new Discount("", (byte) 0);
        if (user != null && user.getBirthDate() != null) {
            int days = Period.between(LocalDate.from(airDateTime), user.getBirthDate()).getDays();
            if (Math.abs(days) <= 5) {
                discount = new Discount(STRATEGY_NAME, percent);
            }
        }
        for (int i = 0; i < numberOfTickets; i++) {
            discountList.add(discount);
        }
        return discountList;
    }
}
