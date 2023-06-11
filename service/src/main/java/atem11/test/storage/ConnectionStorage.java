package atem11.test.storage;

import atem11.test.model.DbConnection;

public interface ConnectionStorage {
    void saveConnection(String alias, DbConnection dbConnection);
    void removeConnection(String alias);
    DbConnection getConnection(String alias);
}
