package storage;

import model.DbConnection;

public interface ConnectionStorage {
    void saveConnection(String alias, DbConnection dbConnection);
    void removeConnection(String alias);
    DbConnection getConnection(String alias);
}
