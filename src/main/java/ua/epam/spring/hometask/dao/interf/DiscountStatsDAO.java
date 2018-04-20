package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.DiscountStats;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface DiscountStatsDAO extends DomainObjectDAO<DiscountStats> {
    List<DiscountStats> getByUserId(Long userId);

    List<DiscountStats> getByDiscountType(String discountType);

    DiscountStats getByUserIdAndDiscountType(Long userId, String discountType);
}
