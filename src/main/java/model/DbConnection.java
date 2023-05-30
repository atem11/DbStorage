package model;

public record DbConnection(
        String connectionURL,
        String username,
        String password
) {
}
