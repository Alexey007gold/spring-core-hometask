package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.interf.EventCounterStatsService;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class CounterAspect {

    private EventCounterStatsService eventCounterStatsService;

    public CounterAspect(EventCounterStatsService eventCounterStatsService) {
        this.eventCounterStatsService = eventCounterStatsService;
    }

    @AfterReturning(value = "execution(* ua.epam.spring.hometask.dao.interf.EventDAO.getByName(..))", returning = "retEvent")
    public void eventGetByNameCounter(Event retEvent) {
        if (retEvent != null) {
            eventCounterStatsService.incrementAccessByNameCount(retEvent);
        }
    }

    @Before(value = "execution(* ua.epam.spring.hometask.service.interf.BookingService.getTicketsPriceWithDiscount(..))")
    public void eventPriceQueryCounter(JoinPoint joinPoint) {
        eventCounterStatsService.incrementPriceQueryCount((Event) joinPoint.getArgs()[0]);
    }

    @Before(value = "execution(* ua.epam.spring.hometask.service.interf.BookingService.bookTickets(..))")
    @SuppressWarnings("unchecked")
    public void eventBookTicketsCounter(JoinPoint joinPoint) {
        for (Ticket ticket : ((List<Ticket>) joinPoint.getArgs()[0])) {
            eventCounterStatsService.incrementTicketsBookedTimesCount(ticket.getEvent());
        }
    }
}
