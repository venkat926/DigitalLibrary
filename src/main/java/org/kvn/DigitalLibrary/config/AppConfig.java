package org.kvn.DigitalLibrary.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.kvn.DigitalLibrary.constants.Constants.*;

@Configuration
public class AppConfig {

    @Bean("connection")
    public Connection connection() {
        try {
            return DriverManager.getConnection(MYSQL_DB_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName(DRIVER_NAME);
        builder.url(MYSQL_DB_URL);
        builder.username(USER_NAME);
        builder.password(PASSWORD);
        return builder.build();

        // same as above
//         return DataSourceBuilder.create()
//                 .driverClassName(DRIVER_NAME)
//                 .url(MYSQL_DB_URL)
//                 .username(USER_NAME)
//                 .password(PASSWORD)
//                 .build();

    }
}
