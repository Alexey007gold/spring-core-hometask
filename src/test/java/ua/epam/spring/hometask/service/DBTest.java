package ua.epam.spring.hometask.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Oleksii_Kovetskyi on 4/6/2018.
 */
public abstract class DBTest {

    public JdbcTemplate getJDBCTemplate() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(props.getProperty("db.driver"));
        dataSource.setUrl(props.getProperty("db.path"));
        dataSource.setUsername(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.pass"));

        return new JdbcTemplate(dataSource);
    }
}
