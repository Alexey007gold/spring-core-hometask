package ua.epam.spring.hometask.domain;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public class EventStats extends DomainObject {

    private long eventId;

    private long accessByName;

    private long priceQuery;

    private long ticketsBooked;

    public EventStats() {
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getAccessByName() {
        return accessByName;
    }

    public void setAccessByName(long accessByName) {
        this.accessByName = accessByName;
    }

    public void incrementAccessByName() {
        this.accessByName++;
    }

    public long getPriceQuery() {
        return priceQuery;
    }

    public void setPriceQuery(long priceQuery) {
        this.priceQuery = priceQuery;
    }

    public void incrementPriceQuery() {
        this.priceQuery++;
    }

    public long getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(long ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void incrementTicketsBookedTimes() {
        this.ticketsBooked++;
    }
}
