package ua.epam.spring.hometask.domain;

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
        this.balance = balance;
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
        this.balance = balance;
    }
}
