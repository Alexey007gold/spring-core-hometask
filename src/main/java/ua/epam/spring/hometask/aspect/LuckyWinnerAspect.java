package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.Random;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class LuckyWinnerAspect {

    public static int bound = 1000;
    private Random random = new Random();

    @Before(value = "execution(* ua.epam.spring.hometask.service.BookingService.bookTicket(..))")
    public void bookTicket(JoinPoint joinPoint) {
        if (isLucky()) {
            ((Ticket) joinPoint.getArgs()[0]).setPrice(0);
        }
    }

    @Before(value = "execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    public void bookTickets(JoinPoint joinPoint) {
        if (isLucky()) {
            Set<Ticket> tickets = (Set<Ticket>) joinPoint.getArgs()[0];
            tickets.stream().findFirst().ifPresent(ticket -> ticket.setPrice(0));
        }
    }

    private boolean isLucky() {
        return random.nextInt(bound) == 0;
    }
}
