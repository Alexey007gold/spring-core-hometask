package ua.epam.spring.hometask.exception;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException(double required, double balance) {
        super(String.format("Not enough money. You have %.2f, but required %.2f", balance, required));
    }
}
