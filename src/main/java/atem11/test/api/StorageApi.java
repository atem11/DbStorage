package atem11.test.api;


public interface StorageApi {
    void addConnection(
            String alias,
            String connectionURL,
            String username,
            String password
    );

    void removeConnection(
            String alias
    );

    void testConnection(
            String alias
    );

    String execute(
            String alias,
            String statement
    );
}
