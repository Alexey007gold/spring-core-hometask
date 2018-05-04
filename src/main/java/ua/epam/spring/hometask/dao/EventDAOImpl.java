package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.AuditoriumDAO;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.dao.interf.EventDateDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.domain.EventRating;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class EventDAOImpl extends AbstractDomainObjectDAO<Event> implements EventDAO {

    private AuditoriumDAO auditoriumDAO;
    private EventDateDAO eventDateDAO;

    public EventDAOImpl(JdbcTemplate jdbcTemplate, AuditoriumDAO auditoriumDAO, EventDateDAO eventDateDAO) {
        super(jdbcTemplate);
        this.auditoriumDAO = auditoriumDAO;
        this.eventDateDAO = eventDateDAO;
    }

    private RowMapper<EventDate> eventTimeRowMapper = (resultSet, i) ->
            new EventDate(resultSet.getTimestamp(3).toLocalDateTime(),
                    auditoriumDAO.getByName(resultSet.getString(4)));

    private RowMapper<Event> rowMapper = (resultSet, i) -> {
        Event event = new Event();
        event.setId(resultSet.getLong(1));
        event.setName(resultSet.getString(2));
        event.setRating(EventRating.values()[resultSet.getInt(3)]);
        event.setBasePrice(resultSet.getDouble(4));

        String sql = String.format("SELECT * FROM event_dates WHERE event_id = %d", event.getId());
        TreeSet<EventDate> airDates = new TreeSet<>(jdbcTemplate.query(sql, eventTimeRowMapper));

        event.setAirDates(airDates);
        return event;
    };

    @Override
    public void remove(@Nonnull Event event) {
        super.remove(event);
        for (EventDate eventDate : eventDateDAO.getBy(new String[]{"event_id"}, event.getId())) {
            eventDateDAO.remove(eventDate);
        }
    }

    public Event getByName(String name) {
        return getOneBy(new String[] {"name"}, name);
    }

    @Override
    protected RowMapper<Event> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "events";
    }

    @Override
    public void save(Event event) {
        if (event.getId() != null) {
            update(event);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (name, rating, base_price) VALUES (?, ?, ?)", getTableName());
                PreparedStatement ps =con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, event.getName());
                ps.setInt(2, event.getRating().ordinal());
                ps.setDouble(3, event.getBasePrice());
                return ps;
            }, keyHolder);

            event.setId((Long) keyHolder.getKeys().get("id"));
        }

        saveEventAirDates(event);
    }

    private void update(Event event) {
        String sql = String.format("UPDATE %s SET name = ?, rating = ?, base_price = ? WHERE id = %d",
                getTableName(), event.getId());
        jdbcTemplate.update(sql, event.getName(), event.getRating().ordinal(), event.getBasePrice());
    }

    private void saveEventAirDates(Event event) {
        List<Object[]> eventTimes = event.getAirDates().values().stream()
                .filter(airDate -> eventDateDAO.getById(airDate.getId()) == null)
                .map(airDate -> new Object[]{event.getId(),
                        Timestamp.valueOf(airDate.getDateTime()),
                        airDate.getAuditorium().getName()})
                .collect(toList());
        String sql = "INSERT INTO event_dates (event_id, time, auditorium_name) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, eventTimes);
    }
}
