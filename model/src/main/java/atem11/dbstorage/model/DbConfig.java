package atem11.dbstorage.model;

public class DbConfig {
    private final String connectionURL;
    private final String username;
    private final String password;
    private final Integer maxPoolSize;

    public DbConfig(String connectionURL, String username, String password, Integer maxPoolSize) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    public String connectionURL() {
        return connectionURL;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public Integer maxPoolSize() {
        return maxPoolSize;
    }
}
