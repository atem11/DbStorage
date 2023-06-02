package atem11.test.model;

public class DbConnection {
    private final String connectionURL;
    private final String username;
    private final String password;

    public DbConnection(
            String connectionURL,
            String username,
            String password) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
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
}
