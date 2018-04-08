package ua.epam.spring.hometask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.DiscountStatsDAO;
import ua.epam.spring.hometask.domain.DiscountStats;
import ua.epam.spring.hometask.domain.User;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Service
public class DiscountCounterServiceImpl implements DiscountCounterService {

    @Autowired
    private DiscountStatsDAO discountStatsDAO;

    @Override
    public void count(String className, User user) {
        DiscountStats userStats = discountStatsDAO.getByUserIdAndDiscountType(user.getId(), className);
        DiscountStats totalStats = discountStatsDAO.getByUserIdAndDiscountType(null, className);
        increment(userStats);
        increment(totalStats);
    }

    private void increment(DiscountStats stats) {
        if (stats == null) {
            stats = new DiscountStats();
        }
        stats.increment();
        discountStatsDAO.save(stats);
    }
}
