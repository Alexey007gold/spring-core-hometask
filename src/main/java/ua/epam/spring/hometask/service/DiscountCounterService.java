package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.DiscountStats;
import ua.epam.spring.hometask.domain.User;

import java.util.Collection;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public interface DiscountCounterService {
    void count(String className, User user);
    Collection<DiscountStats> getDiscountStatsForUser(User user);
    Collection<DiscountStats> getDiscountStatsForType(String discountType);
    long getTotalDiscountCountForType(String discountType);
}
