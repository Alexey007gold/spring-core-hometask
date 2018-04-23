package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.interf.DiscountCounterService;

import java.util.List;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class DiscountAspect {

    private DiscountCounterService discountCounterService;

    public DiscountAspect(DiscountCounterService discountCounterService) {
        this.discountCounterService = discountCounterService;
    }

    @Around(value = "execution(* ua.epam.spring.hometask.service.interf.BookingService.bookTickets(..))")
    @SuppressWarnings("unchecked")
    public Set<Integer> discountCounter(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Ticket> tickets = (List<Ticket>) joinPoint.getArgs()[0];
        List<Discount> discounts = (List<Discount>) joinPoint.getArgs()[1];

        Set<Integer> bookedTickets = (Set<Integer>) joinPoint.proceed();

        for (int i = 0; i < discounts.size(); i++) {
            if (discounts.get(i).getPercent() != 0) {
                discountCounterService.count(discounts.get(i).getDiscountType(), tickets.get(i).getUser());
            }
        }
        return bookedTickets;
    }
}
