package ua.epam.spring.hometask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.dao.interf.TicketDAO;
import ua.epam.spring.hometask.dao.interf.UserDAO;
import ua.epam.spring.hometask.domain.Ticket;

import java.sql.*;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
@Component
public class TicketDAOImpl extends AbstractDomainObjectDAO<Ticket> implements TicketDAO {

    private UserDAO userDAO;
    private EventDAO eventDAO;

    public TicketDAOImpl(JdbcTemplate jdbcTemplate, EventDAO eventDAO) {
        super(jdbcTemplate);
        this.eventDAO = eventDAO;
    }

    private RowMapper<Ticket> rowMapper = new RowMapper<Ticket>() {
        @Override
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ticket ticket = new Ticket(userDAO.getById(rs.getLong(2)),
                    eventDAO.getById(rs.getLong(3)),
                    rs.getTimestamp(4).toLocalDateTime(),
                    rs.getInt(5),
                    rs.getDouble(6));
            ticket.setId(rs.getLong(1));

            return ticket;
        }
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

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
