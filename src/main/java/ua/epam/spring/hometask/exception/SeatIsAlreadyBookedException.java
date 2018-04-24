package ua.epam.spring.hometask.exception;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
public class SeatIsAlreadyBookedException extends RuntimeException {

    public SeatIsAlreadyBookedException(int seat) {
        super(String.format("Seat %d is already booked", seat));
    }
}
