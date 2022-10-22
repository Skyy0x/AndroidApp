package pl.sky0x.app.data.implementation.mysql;

public class DatabaseInfo {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public DatabaseInfo(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
