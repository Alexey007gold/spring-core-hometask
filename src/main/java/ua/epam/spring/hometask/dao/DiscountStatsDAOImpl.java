package ua.epam.spring.hometask.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.DiscountStatsDAO;
import ua.epam.spring.hometask.domain.DiscountStats;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Component
public class DiscountStatsDAOImpl extends AbstractDomainObjectDAO<DiscountStats> implements DiscountStatsDAO {

    private RowMapper<DiscountStats> rowMapper = (rs, rowNum) -> {
        DiscountStats discountStats = new DiscountStats();
        discountStats.setId(rs.getLong(1));
        discountStats.setDiscountType(rs.getString(2));
        discountStats.setUserId(rs.getLong(3));
        discountStats.setTimes(rs.getInt(4));
        return discountStats;
    };

    public DiscountStatsDAOImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<DiscountStats> getByUserId(Long userId) {
        return getBy(new String[] {"user_id"}, userId);
    }

    @Override
    public List<DiscountStats> getByDiscountType(String discountType) {
        return getBy(new String[] {"discount_type"}, discountType);
    }

    @Override
    public DiscountStats getByUserIdAndDiscountType(Long userId, String discountType) {
        return getOneBy(new String[] {"user_id", "discount_type"}, userId, discountType);
    }

    @Override
    protected RowMapper<DiscountStats> getRowMapper() {
        return rowMapper;
    }

    @Override
    protected String getTableName() {
        return "discount_stats";
    }

    @Override
    public void save(DiscountStats stats) {
        if (stats.getId() != null) {
            update(stats);
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                String sql = String.format("INSERT INTO %s (discount_type, user_id, times) VALUES (?, ?, ?)", getTableName());
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, stats.getDiscountType());
                ps.setObject(2, stats.getUserId());
                ps.setInt(3, stats.getTimes());
                return ps;
            }, keyHolder);

            stats.setId((Long) keyHolder.getKeys().get("id"));
        }
    }

    private void update(DiscountStats stats) {
        String sql = String.format("UPDATE %s SET discount_type = ?, user_id = ?, times = ? WHERE id = %d",
                getTableName(), stats.getId());

        jdbcTemplate.update(sql, stats.getDiscountType(), stats.getUserId(), stats.getTimes());
    }
}
