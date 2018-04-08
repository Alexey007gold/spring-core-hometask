package ua.epam.spring.hometask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

import java.sql.*;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class TicketDAO extends DomainObjectDAO<Ticket> {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    private RowMapper<Ticket> rowMapper = new RowMapper<Ticket>() {
        @Override
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ticket ticket = new Ticket(userService.getById(rs.getLong(2)),
                    eventService.getById(rs.getLong(3)),
                    rs.getTimestamp(4).toLocalDateTime(), rs.getLong(5));
            ticket.setId(rs.getLong(1));

            return ticket;
        }
    };

    public TicketDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<Ticket> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "ticket";
    }

    @Override
    public void save(Ticket ticket) {
        if (ticket.getId() != null) {
            update(ticket);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = "INSERT INTO tickets (user_id, event_id, time, seat) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, ticket.getUser().getId());
                ps.setLong(2, ticket.getEvent().getId());
                ps.setTimestamp(3, Timestamp.valueOf(ticket.getDateTime()));
                ps.setLong(4, ticket.getSeat());
                return ps;
            }, keyHolder);

            ticket.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(Ticket ticket) {
        String sql = String.format("UPDATE %s SET user_id = ?, event_id = ?, time = ?, seat = ? WHERE id = %d",
                getTableName(), ticket.getId());

        jdbcTemplate.update(sql, ticket.getUser().getId(), ticket.getEvent().getId(),
                Timestamp.valueOf(ticket.getDateTime()), ticket.getSeat());
    }
}
