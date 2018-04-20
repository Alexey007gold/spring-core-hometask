package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.UserRoleDAO;
import ua.epam.spring.hometask.domain.UserRole;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
@Component
public class UserRoleDAOImpl extends AbstractDomainObjectDAO<UserRole> implements UserRoleDAO {

    private RowMapper<UserRole> rowMapper =
            (resultSet, i) -> {
                UserRole userRole = new UserRole(resultSet.getLong(2), resultSet.getString(3));
                userRole.setId(resultSet.getLong(1));
                return userRole;
            };

    public UserRoleDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<UserRole> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "user_roles";
    }

    @Override
    public void save(UserRole userRole) {
        if (userRole.getId() != null) {
            update(userRole);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (user_id, role) VALUES (?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userRole.getUserId());
                ps.setString(2, userRole.getRole());
                return ps;
            }, keyHolder);

            userRole.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(UserRole userRole) {
        String sql = String.format("UPDATE %s SET user_id = ?, times = ? WHERE id = %d",
                getTableName(), userRole.getId());

        jdbcTemplate.update(sql, userRole.getUserId(), userRole.getRole());
    }
}
