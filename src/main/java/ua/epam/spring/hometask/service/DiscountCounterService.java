package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.User;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public interface DiscountCounterService {
    void count(String className, User user);
}
