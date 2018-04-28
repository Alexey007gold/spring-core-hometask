package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public class EventDate extends DomainObject implements Comparable<EventDate> {

    private Long eventId;
    private LocalDateTime dateTime;
    private Auditorium auditorium;

    public EventDate() {
    }

    public EventDate(LocalDateTime dateTime, Auditorium auditorium) {
        this.dateTime = dateTime;
        this.auditorium = auditorium;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public long getDateTimeAsSeconds() {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium = auditorium;
    }

    @Override
    public int compareTo(EventDate o) {
        return this.getDateTime().compareTo(o.getDateTime());
    }
}
