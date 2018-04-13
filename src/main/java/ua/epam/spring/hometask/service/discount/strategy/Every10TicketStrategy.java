package ua.epam.spring.hometask.service.discount.strategy;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

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
    public Discount getDiscount(@Nullable User user, @Nonnull Event event,
                                @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        if (numberOfTickets == 0) return null;

        Discount disc = null;
        double percentFor10thTicket = (50.0 / numberOfTickets);
        if (isUserRegistered(user)) {
            int boughtTickets = user.getTickets().size();

            int a = boughtTickets / 10;
            int b = (boughtTickets + (int) numberOfTickets) / 10;
            int numberOfTicketsWithDiscount = b - a;
            byte resultingDiscount = (byte) (percentFor10thTicket * numberOfTicketsWithDiscount);
            if (resultingDiscount != 0) {
                disc = new Discount("Every10TicketStrategy", resultingDiscount);
            }
        } else if (numberOfTickets >= 10) {
            disc = new Discount("Every10TicketStrategy", (byte) percentFor10thTicket);
        }
        return disc;
    }

    private boolean isUserRegistered(@Nullable User user) {
        return user != null && user.getId() != null && userService.getById(user.getId()) != null;
    }
}
