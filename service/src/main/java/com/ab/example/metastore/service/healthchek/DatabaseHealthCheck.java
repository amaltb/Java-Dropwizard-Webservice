package com.ab.example.metastore.service.healthchek;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.db.DataSourceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author _amal
 *
 * Class implementing health check methods for database connection.
 */
public class DatabaseHealthCheck extends HealthCheck {

    private final DataSourceFactory dbConfig;

    public DatabaseHealthCheck(DataSourceFactory dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    protected Result check() throws Exception {
        Class.forName(dbConfig.getDriverClass());
        try (Connection c = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
             Statement st = c.createStatement()) {
            st.execute(dbConfig.getValidationQuery());
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy("Exception while performing health check: " + e);
        }
    }
}
