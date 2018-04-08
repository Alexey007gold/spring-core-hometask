package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.EventCounterStatsService;

import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class CounterAspect {

    @Autowired
    private EventCounterStatsService eventCounterStatsService;

    @AfterReturning(value = "execution(* ua.epam.spring.hometask.dao.EventDAO.getByName(..))", returning = "retEvent")
    public void eventGetByNameCounter(Event retEvent) {
        eventCounterStatsService.incrementAccessByNameCount(retEvent);
    }

    @Before(value = "execution(* ua.epam.spring.hometask.service.BookingService.getTicketsPrice(..))")
    public void eventPriceQueryCounter(JoinPoint joinPoint) {
        eventCounterStatsService.incrementPriceQueryCount((Event) joinPoint.getArgs()[0]);
    }

    @Before(value = "execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    public void eventBookTicketsCounter(JoinPoint joinPoint) {
        for (Ticket ticket : ((Set<Ticket>) joinPoint.getArgs()[0])) {
            eventCounterStatsService.incrementPriceQueryCount(ticket.getEvent());
        }
    }
}