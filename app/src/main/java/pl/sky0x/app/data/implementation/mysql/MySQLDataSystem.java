package pl.sky0x.app.data.implementation.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import pl.sky0x.app.data.Click;
import pl.sky0x.app.data.DataSystem;

public class MySQLDataSystem implements DataSystem {

    private final MySQLService sqlService;

    public MySQLDataSystem(MySQLService sqlService) {
        this.sqlService = sqlService;
    }

    @Override
    public Collection<Click> getClicks() {
        try (ResultSet set = sqlService.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM clicks")) {

            Collection<Click> clicks = new LinkedList<>();

            while (set.next()) {
                Click click = new Click(
                        UUID.fromString(set.getString("id")),
                        set.getLong("time"),
                        set.getString("ip")
                );

                clicks.add(click);
            }

            return clicks;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean addClick(Click click) {
        try (PreparedStatement statement = sqlService.getConnection()
                .prepareStatement("INSERT INTO clicks (id, time, ip) ON DUPLICATE KEY UPDATE id = VALUES(id), time = VALUES(time), ip = VALUES(ip)")) {

            statement.setString(1, click.getUuid().toString());
            statement.setLong(2, click.getTime());
            statement.setString(3, click.getIp());

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
