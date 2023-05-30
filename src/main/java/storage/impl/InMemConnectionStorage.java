package storage.impl;

import model.DbConnection;
import org.springframework.stereotype.Component;
import storage.ConnectionStorage;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemConnectionStorage implements ConnectionStorage {
    private final Map<String, DbConnection> connectionStor;

    public InMemConnectionStorage() {
        System.out.println("InMemConnectionStorage constructor");
        this.connectionStor = new HashMap<>();
    }

    @Override
    public void saveConnection(String alias, DbConnection dbConnection) {
        connectionStor.put(alias, dbConnection);
    }

    @Override
    public void removeConnection(String alias) {
        connectionStor.remove(alias);
    }

    @Override
    public DbConnection getConnection(String alias) {
        return connectionStor.get(alias);
    }
}
