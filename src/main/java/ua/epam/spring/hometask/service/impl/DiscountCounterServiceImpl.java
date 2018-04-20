package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.interf.DiscountStatsDAO;
import ua.epam.spring.hometask.domain.DiscountStats;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.DiscountCounterService;

import java.util.Collection;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Service
public class DiscountCounterServiceImpl implements DiscountCounterService {

    @Autowired
    private DiscountStatsDAO discountStatsDAO;

    @Override
    public void count(String type, User user) {
        DiscountStats userStats = discountStatsDAO.getByUserIdAndDiscountType(user != null ? user.getId() : null, type);
        DiscountStats totalStats = discountStatsDAO.getByUserIdAndDiscountType(null, type);
        increment(type, user, userStats);
        increment(type, null, totalStats);
    }

    @Override
    public Collection<DiscountStats> getDiscountStatsForUser(User user) {
        return discountStatsDAO.getByUserId(user.getId());
    }

    @Override
    public Collection<DiscountStats> getDiscountStatsForType(String discountType) {
        return discountStatsDAO.getByDiscountType(discountType);
    }

    @Override
    public long getTotalDiscountCountForType(String discountType) {
        return discountStatsDAO.getByUserIdAndDiscountType(null, discountType).getTimes();
    }

    private void increment(String type, User user, DiscountStats stats) {
        if (stats == null) {
            stats = new DiscountStats();
            stats.setDiscountType(type);
            stats.setUserId(user != null ? user.getId() : null);
        }
        stats.increment();
        discountStatsDAO.save(stats);
    }
}
