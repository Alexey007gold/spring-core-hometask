package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.List;
import java.util.Random;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class LuckyWinnerAspect {

    public static int bound = 1000;
    private Random random = new Random();

    @Before(value = "execution(* ua.epam.spring.hometask.service.interf.BookingService.bookTickets(..))")
    @SuppressWarnings("unchecked")
    public void bookTickets(JoinPoint joinPoint) {
        if (isLucky()) {
            List<Ticket> tickets = (List<Ticket>) joinPoint.getArgs()[0];
            tickets.stream().findFirst().ifPresent(ticket -> ticket.setPrice(0));
        }
    }

    private boolean isLucky() {
        return random.nextInt(bound) == 0;
    }
}
