package ua.epam.spring.hometask.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
public class UserAccount extends DomainObject {

    private Long userId;
    private double balance;

    public UserAccount() {
    }

    public UserAccount(Long userId, double balance) {
        this.userId = userId;
        setBalance(balance);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance)
            .setScale(2, RoundingMode.FLOOR)
            .doubleValue();
    }
}
