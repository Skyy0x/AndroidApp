package pl.sky0x.app.data.implementation.mysql;

import android.annotation.SuppressLint;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLService {

    private final DatabaseInfo databaseInfo;

    private Connection connection;

    public MySQLService(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    //Connecting to database
    public Connection connect() throws SQLException {
        return connection = new HikariDataSource(getHikariConfig()).getConnection();
    }

    //Creating table
    public void createTable() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `clicks` " +
                    "(`id` VARCHAR(36), `time` BIGINT(19), `ip` VARCHAR(36), PRIMARY KEY(id))");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                databaseInfo.getHost(),
                databaseInfo.getPort(),
                databaseInfo.getDatabase()));
        hikariConfig.setUsername(databaseInfo.getUsername());
        hikariConfig.setPassword(databaseInfo.getPassword());

        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
        hikariConfig.addDataSourceProperty("maintainTimeStats", "false");
        return hikariConfig;
    }

    public Connection getConnection() {
        return connection;
    }
}
