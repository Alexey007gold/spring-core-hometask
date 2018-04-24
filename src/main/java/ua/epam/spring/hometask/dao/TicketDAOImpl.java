package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.TicketDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class TicketDAOImpl extends AbstractDomainObjectDAO<Ticket> implements TicketDAO {

    public TicketDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private RowMapper<Ticket> rowMapper = (rs, rowNum) -> {
        User user = null;
        if (rs.getObject(5) != null) {
            user = new User();
            user.setId(rs.getLong(5));
            user.setFirstName(rs.getString(6));
            user.setLastName(rs.getString(7));
            user.setEmail(rs.getString(8));
            user.setLogin(rs.getString(9));
            user.setPassword(rs.getString(10));
            Date date = rs.getDate(11);
            if (date != null) {
                user.setBirthDate(date.toLocalDate());
            }
        }

        Event event = null;
        if (rs.getObject(12) != null) {
            event = new Event();
            event.setId(rs.getLong(12));
            event.setName(rs.getString(13));
            event.setRating(EventRating.values()[rs.getInt(14)]);
            event.setBasePrice(rs.getDouble(15));
        }

        Ticket ticket = new Ticket(user, event,
                rs.getTimestamp(2).toLocalDateTime(),
                rs.getInt(3),
                rs.getDouble(4));
        ticket.setId(rs.getLong(1));

        return ticket;
    };

    @Override
    protected RowMapper<Ticket> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "tickets";
    }

    @Override
    protected String getColumnNamesLine() {
        return "tickets.id, tickets.time, tickets.seat, tickets.price, " +
                "users.id, users.first_name, users.last_name, users.email, users.login, users.password, users.birth_date, " +
                "events.id, events.name, events.rating, events.base_price";
    }

    @Override
    protected String getJoinLine() {
        return "LEFT JOIN users ON tickets.user_id=users.id LEFT JOIN events ON tickets.event_id=events.id";
    }

    @Override
    public void save(Ticket ticket) {
        if (ticket.getId() != null) {
            update(ticket);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (user_id, event_id, time, seat, price) VALUES (?, ?, ?, ?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                if (ticket.getUser() != null) {
                    ps.setLong(1, ticket.getUser().getId());
                } else {
                    ps.setObject(1, null);
                }
                ps.setLong(2, ticket.getEvent().getId());
                ps.setTimestamp(3, Timestamp.valueOf(ticket.getDateTime()));
                ps.setInt(4, ticket.getSeat());
                ps.setDouble(5, ticket.getPrice());
                return ps;
            }, keyHolder);

            ticket.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(Ticket ticket) {
        String sql = String.format("UPDATE %s SET user_id = ?, event_id = ?, time = ?, seat = ?, price = ? WHERE id = %d",
                getTableName(), ticket.getId());

        jdbcTemplate.update(sql, ticket.getUser().getId(), ticket.getEvent().getId(),
                Timestamp.valueOf(ticket.getDateTime()), ticket.getSeat(), ticket.getPrice());
    }

    @Override
    public List<Integer> getBookedSeatsForEventAndDate(Long eventId, LocalDateTime dateTime) {
        String sql = "SELECT seat FROM tickets WHERE event_id = ? AND time = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{eventId, dateTime}, Integer.class);
    }
}
