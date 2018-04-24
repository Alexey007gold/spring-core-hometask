package ua.epam.spring.hometask.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.epam.spring.hometask.dao.interf.DomainObjectDAO;
import ua.epam.spring.hometask.domain.DomainObject;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public abstract class AbstractDomainObjectDAO<T extends DomainObject> implements DomainObjectDAO<T> {

    protected JdbcTemplate jdbcTemplate;

    public AbstractDomainObjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected abstract RowMapper<T> getRowMapper();

    protected abstract String getTableName();

    protected String getColumnNamesLine() {
        return "*";
    }

    protected String getJoinLine() {
        return "";
    }

    @Override
    public List<T> getAll() {
        String sql = String.format("SELECT %s FROM %s %s",
                getColumnNamesLine(), getTableName(), getJoinLine());
        return jdbcTemplate.query(sql, getRowMapper());
    }

    @Override
    public void remove(T object) {
        try {
            String sql = String.format("DELETE FROM %s WHERE id = %d", getTableName(), object.getId());
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {}
    }

    @Override
    public T getById(Long id) {
        try {
            String sql = String.format("SELECT %s FROM %s %s WHERE id = %d",
                    getColumnNamesLine(), getTableName(), getJoinLine(), id);
            return jdbcTemplate.queryForObject(sql, getRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<T> getBy(String[] columnNames, Object... values) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ").append(getColumnNamesLine()).append(" FROM ").append(getTableName())
                .append(" ").append(getJoinLine()).append(" WHERE ");
        for (int i = 0; i < columnNames.length; i++) {
            builder.append(columnNames[i]).append(" = ?");
            if (i + 1 < columnNames.length) {
                builder.append(" and ");
            }
        }
        try {
            return jdbcTemplate.query(builder.toString(), values, getRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public T getOneBy(String[] columnNames, Object... values) {
        List<T> list = getBy(columnNames, values);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
