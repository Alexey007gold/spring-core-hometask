package ua.epam.spring.hometask.domain;

import ua.epam.spring.hometask.soap.xmladapter.AirDatesMapAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * @author Yuriy_Tkach
 */
@XmlType(name = "Event", propOrder = {"name", "basePrice", "rating", "airDates"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Event extends DomainObject {

    private String name;

    private double basePrice;

    private EventRating rating;

    @XmlJavaTypeAdapter(value = AirDatesMapAdapter.class)
    private Map<LocalDateTime, EventDate> airDates = new HashMap<>();

    /**
     * Adding date and time of event air and assigning auditorium to that
     * 
     * @param eventDate EventDate object
     * @return <code>true</code> if successful, <code>false</code> if already
     *         there
     */
    public boolean addAirDateTime(EventDate eventDate) {
        if (airDates.containsKey(eventDate.getDateTime()))
            return false;

        airDates.put(eventDate.getDateTime(), eventDate);
        return true;
    }

    /**
     * Removes the date and time of event air. If auditorium was assigned to
     * that date and time - the assignment is also removed
     * 
     * @param dateTime
     *            Date and time to remove
     * @return <code>true</code> if successful, <code>false</code> if not there
     */
    public boolean removeAirDateTime(LocalDateTime dateTime) {
        return airDates.remove(dateTime) != null;
    }

    /**
     * Checks if event airs on particular date and time
     * 
     * @param dateTime
     *            Date and time to check
     * @return <code>true</code> event airs on that date and time
     */
    public boolean airsOnDateTime(LocalDateTime dateTime) {
        return airDates.keySet().stream().anyMatch(dt -> dt.equals(dateTime));
    }

    /**
     * Checks if event airs on particular date
     * 
     * @param date
     *            Date to ckeck
     * @return <code>true</code> event airs on that date
     */
    public boolean airsOnDate(LocalDate date) {
        return airDates.keySet().stream().anyMatch(dt -> dt.toLocalDate().equals(date));
    }

    /**
     * Checking if event airs on dates between <code>from</code> and
     * <code>to</code> inclusive
     * 
     * @param from
     *            Start date to check
     * @param to
     *            End date to check
     * @return <code>true</code> event airs on dates
     */
    public boolean airsOnDates(LocalDate from, LocalDate to) {
        return airDates.keySet().stream()
                .anyMatch(dt -> dt.toLocalDate().compareTo(from) >= 0 && dt.toLocalDate().compareTo(to) <= 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public EventRating getRating() {
        return rating;
    }

    public void setRating(EventRating rating) {
        this.rating = rating;
    }

    public Map<LocalDateTime, EventDate> getAirDates() {
        return airDates;
    }

    public void setAirDates(Map<LocalDateTime, EventDate> airDates) {
        this.airDates = airDates;
    }

    public void setAirDates(Set<EventDate> airDates) {
        this.airDates = new TreeMap<>(airDates.stream().collect(toMap(EventDate::getDateTime, d -> d)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
