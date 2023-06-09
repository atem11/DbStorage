package atem11.test.storage.impl;

import atem11.test.model.DbConfig;
import atem11.test.model.DbConnection;
import atem11.test.storage.ConnectionStorage;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.HashMap;
import java.util.Map;

public class DbConnectionStorage implements ConnectionStorage {
    private static final String VALIDATION_QUERY_SQL = "select 1";

    public DbConnectionStorage(DbConfig dbConfig) {
        var dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(dbConfig.connectionURL());
        dataSource.setUser(dbConfig.username());
        dataSource.setPassword(dbConfig.password());
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(dbConfig.maxPoolSize());
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setPreferredTestQuery(VALIDATION_QUERY_SQL);
    }

    @Override
    public void saveConnection(String alias, DbConnection dbConnection) {

    }

    @Override
    public void removeConnection(String alias) {

    }

    @Override
    public DbConnection getConnection(String alias) {
        return null;
    }
}
