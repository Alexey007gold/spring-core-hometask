package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.EventStatsDAO;
import ua.epam.spring.hometask.domain.EventStats;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Component
public class EventStatsDAOImpl extends AbstractDomainObjectDAO<EventStats> implements EventStatsDAO {

    private RowMapper<EventStats> rowMapper = (rs, rowNum) -> {
        EventStats eventStats = new EventStats();
        eventStats.setId(rs.getLong(1));
        eventStats.setEventId(rs.getLong(2));
        eventStats.setAccessByName(rs.getLong(3));
        eventStats.setPriceQuery(rs.getLong(4));
        eventStats.setTicketsBooked(rs.getLong(5));
        return eventStats;
    };

    public EventStatsDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public EventStats getByEventId(Long eventId) {
        return getOneBy(new String[] {"event_id"}, eventId);
    }

    @Override
    protected RowMapper<EventStats> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "event_stats";
    }

    @Override
    public void save(EventStats stats) {
        if (stats.getId() != null) {
            update(stats);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (event_id, access_by_name, price_query, tickets_booked) VALUES (?, ?, ?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, stats.getEventId());
                ps.setLong(2, stats.getAccessByName());
                ps.setLong(3, stats.getPriceQuery());
                ps.setLong(4, stats.getTicketsBooked());
                return ps;
            }, keyHolder);

            stats.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(EventStats stats) {
        String sql = String.format("UPDATE %s SET event_id = ?, access_by_name = ?, price_query = ?, tickets_booked = ? WHERE id = %d",
                getTableName(), stats.getId());

        jdbcTemplate.update(sql, stats.getEventId(), stats.getAccessByName(),
                stats.getPriceQuery(), stats.getTicketsBooked());
    }
}
