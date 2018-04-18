package ua.epam.spring.hometask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.TicketService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class UserDAO extends DomainObjectDAO<User> {

    @Autowired
    private EventService eventService;
    @Autowired
    private TicketDAO ticketDAO;

    private RowMapper<Ticket> ticketRowMapper = (rs, i) -> {
        Ticket ticket = new Ticket(null,
                eventService.getById(rs.getLong(3)),
                rs.getTimestamp(4).toLocalDateTime(), rs.getLong(5),
                rs.getDouble(6));
        ticket.setId(rs.getLong(1));

        return ticket;
    };

    private RowMapper<User> rowMapper = (rs, i) -> {
        User user = new User();
        user.setId(rs.getLong(1));
        user.setFirstName(rs.getString(2));
        user.setLastName(rs.getString(3));
        user.setEmail(rs.getString(4));

        Date date = rs.getDate(5);
        if (date != null) {
            user.setBirthDate(date.toLocalDate());
        }

        String sql = String.format("SELECT * FROM tickets WHERE user_id = %d", user.getId());
        List<Ticket> tickets = jdbcTemplate
                .query(sql, ticketRowMapper);
        for (Ticket ticket : tickets) {
            ticket.setUser(user);
        }

        user.setTickets(new TreeSet<>(tickets));

        return user;
    };

    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    public void save(User user) {
        if (user.getId() != null) {
            update(user);
        } else {//save new user
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (first_name, last_name, email, birth_date) VALUES (?, ?, ?, ?)",
                        getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getEmail());
                ps.setDate(4, user.getBirthDate() != null ? Date.valueOf(user.getBirthDate()) : null);
                return ps;
            }, keyHolder);

            user.setId((Long) keyHolder.getKeys().get("id"));
        }

        saveTickets(user);
    }

    private void update(User user) {
        String sql = String.format("UPDATE %s SET first_name = ?, last_name = ?, email = ?, birth_date = ? WHERE id = %d",
                getTableName(), user.getId());

        jdbcTemplate.update(sql,
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getBirthDate() != null ? Date.valueOf(user.getBirthDate()) : null);
    }

    private void saveTickets(User user) {
        List<Object[]> tickets = user.getTickets().stream()
                .filter(t -> t.getId() == null)
                .map(ticket -> new Object[]{ticket.getUser().getId(),
                        ticket.getEvent().getId(),
                        Timestamp.valueOf(ticket.getDateTime()),
                        ticket.getSeat(),
                        ticket.getPrice()})
                .collect(toList());
        String sql = "INSERT INTO tickets (user_id, event_id, time, seat, price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, tickets);

        user.setTickets(new TreeSet<>(ticketDAO.getBy(new String[] {"user_id"}, user.getId())));
    }

    public User getByEmail(String email) {
        return getOneBy(new String[] {"email"}, email);
    }
}
