package ua.epam.spring.hometask.domain;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public class DiscountStats extends DomainObject {

    private String discountType;
    private Long userId;
    private int times;

    public DiscountStats() {
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public void increment() {
        this.times++;
    }
}
