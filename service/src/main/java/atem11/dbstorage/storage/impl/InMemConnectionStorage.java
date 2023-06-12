package atem11.dbstorage.storage.impl;

import atem11.dbstorage.model.DbConnection;
import atem11.dbstorage.storage.ConnectionStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemConnectionStorage implements ConnectionStorage {
    private final Map<String, DbConnection> connectionStor;

    public InMemConnectionStorage() {
        this.connectionStor = new ConcurrentHashMap<>();
    }

    @Override
    public void saveConnection(String alias, DbConnection dbConnection) {
        connectionStor.putIfAbsent(alias, dbConnection);
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
