package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.DiscountCounterService;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class DiscountAspect {

    @Autowired
    private DiscountCounterService discountCounterService;

    @Around(value = "execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    public void discountCounter(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
        List<Ticket> tickets = (List<Ticket>) joinPoint.getArgs()[0];
        List<Discount> discounts = (List<Discount>) joinPoint.getArgs()[1];
        for (int i = 0; i < discounts.size(); i++) {
            if (discounts.get(i).getPercent() != 0) {
                discountCounterService.count(discounts.get(i).getDiscountType(), tickets.get(i).getUser());
            }
        }
    }
}
