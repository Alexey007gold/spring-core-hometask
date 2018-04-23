package ua.epam.spring.hometask.domain;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public class EventStats extends DomainObject {

    private long eventId;

    private int accessByName;

    private int priceQuery;

    private int ticketsBooked;

    public EventStats() {
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getAccessByName() {
        return accessByName;
    }

    public void setAccessByName(int accessByName) {
        this.accessByName = accessByName;
    }

    public void incrementAccessByName() {
        this.accessByName++;
    }

    public int getPriceQuery() {
        return priceQuery;
    }

    public void setPriceQuery(int priceQuery) {
        this.priceQuery = priceQuery;
    }

    public void incrementPriceQuery() {
        this.priceQuery++;
    }

    public int getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(int ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void incrementTicketsBookedTimes() {
        this.ticketsBooked++;
    }
}
