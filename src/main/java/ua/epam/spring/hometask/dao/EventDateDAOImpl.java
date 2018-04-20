package ua.epam.spring.hometask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.EventDateDAO;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.service.interf.AuditoriumService;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class EventDateDAOImpl extends AbstractDomainObjectDAO<EventDate> implements EventDateDAO {

    @Autowired
    private AuditoriumService auditoriumService;

    private RowMapper<EventDate> rowMapper = ((rs, rowNum) ->
        new EventDate(rs.getTimestamp(2).toLocalDateTime(),
                auditoriumService.getByName(rs.getString(3)))
    );

    public EventDateDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<EventDate> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "event_dates";
    }

    @Override
    public void save(EventDate eventDate) {
        if (eventDate.getId() != null) {
            update(eventDate);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (event_id, time, auditorium_name) VALUES (?, ?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, eventDate.getEventId());
                ps.setTimestamp(2, Timestamp.valueOf(eventDate.getDateTime()));
                ps.setString(3, eventDate.getAuditorium().getName());
                return ps;
            }, keyHolder);

            eventDate.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(EventDate eventDate) {
        String sql = String.format("UPDATE %s SET event_id = ?, time = ?, auditorium_name = ? WHERE id = %d",
                getTableName(), eventDate.getId());

        jdbcTemplate.update(sql, eventDate.getEventId(), Timestamp.valueOf(eventDate.getDateTime()),
                eventDate.getAuditorium().getName());
    }
}
