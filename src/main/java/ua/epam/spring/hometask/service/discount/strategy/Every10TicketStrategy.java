package ua.epam.spring.hometask.service.discount.strategy;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class Every10TicketStrategy implements DiscountStrategy {

    private UserService userService;

    public Every10TicketStrategy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Discount> getDiscount(@Nullable User user, @Nonnull Event event,
                                     @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        if (numberOfTickets == 0) return null;

        List<Discount> discountList = new ArrayList<>((int)numberOfTickets);
        Discount disc = new Discount("Every10TicketStrategy", (byte) 50);
        Discount noDisc = new Discount("Every10TicketStrategy", (byte) 50);
        int boughtTickets = 0;
        if (isUserRegistered(user)) {
            boughtTickets = user.getTickets().size();
        }

        for (long i = 1; i <= numberOfTickets; i++) {
            if ((boughtTickets + i) % 10 == 0) {
                discountList.add(disc);
            } else {
                discountList.add(noDisc);
            }
        }
        return discountList;
    }

    private boolean isUserRegistered(@Nullable User user) {
        return user != null && user.getId() != null && userService.getById(user.getId()) != null;
    }
}
