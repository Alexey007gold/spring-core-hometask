package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.UserAccountDAO;
import ua.epam.spring.hometask.domain.UserAccount;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
@Component
public class UserAccountDAOImpl extends AbstractDomainObjectDAO<UserAccount> implements UserAccountDAO {

    private RowMapper<UserAccount> rowMapper =
            (resultSet, i) -> {
                UserAccount userAccount = new UserAccount(resultSet.getLong(2), resultSet.getDouble(3));
                userAccount.setId(resultSet.getLong(1));
                return userAccount;
            };

    public UserAccountDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<UserAccount> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "user_accounts";
    }

    @Override
    public void save(UserAccount userAccount) {
        if (userAccount.getId() != null) {
            update(userAccount);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (user_id, balance) VALUES (?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userAccount.getUserId());
                ps.setDouble(2, userAccount.getBalance());
                return ps;
            }, keyHolder);

            userAccount.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(UserAccount userAccount) {
        String sql = String.format("UPDATE %s SET user_id = ?, balance = ? WHERE id = %d",
                getTableName(), userAccount.getId());

        jdbcTemplate.update(sql, userAccount.getUserId(), userAccount.getBalance());
    }
}
