package atem11.dbstorage.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DbConnection that = (DbConnection) o;
        return this.connectionURL.equals(that.connectionURL) &&
                this.username.equals(that.username) &&
                this.password.equals(that.password);
    }
}
