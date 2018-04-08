package ua.epam.spring.hometask.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountCounterService;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Aspect
@Component
public class DiscountAspect {

    @Autowired
    private DiscountCounterService discountCounterService;

    @Around(value = "execution(* ua.epam.spring.hometask.service.discount.strategy.DiscountStrategy.getDiscount(..))")
    public void discountCounter(ProceedingJoinPoint joinPoint) throws Throwable {
        byte res = (byte) joinPoint.proceed();
        if (res > 0) {
            String className = joinPoint.getThis().getClass().getSimpleName();
            discountCounterService.count(className, (User) joinPoint.getArgs()[0]);
        }
    }
}
