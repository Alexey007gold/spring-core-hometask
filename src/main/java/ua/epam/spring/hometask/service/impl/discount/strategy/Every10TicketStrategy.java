package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class Every10TicketStrategy implements DiscountStrategy {

    private static final String STRATEGY_NAME = "Every10TicketStrategy";
    private UserService userService;
    private byte percent;

    public Every10TicketStrategy(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("auditoriums.properties"));

        percent = Byte.parseByte(props.getProperty("discount.every10Ticket"));
    }

    @Override
    public List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                                     @Nonnull LocalDateTime airDateTime, int numberOfTickets) {
        if (numberOfTickets == 0) return Collections.emptyList();

        List<Discount> discountList = new ArrayList<>(numberOfTickets);
        Discount disc = new Discount(STRATEGY_NAME, percent);
        Discount noDisc = new Discount("", (byte) 0);
        int boughtTickets = 0;
        if (userService.isUserRegistered(user)) {
            boughtTickets = user.getTickets().size();
        }

        for (int i = 1; i <= numberOfTickets; i++) {
            if ((boughtTickets + i) % 10 == 0) {
                discountList.add(disc);
            } else {
                discountList.add(noDisc);
            }
        }
        return discountList;
    }
}
