package atem11.dbstorage.storage.impl;

import atem11.dbstorage.model.DbConfig;
import atem11.dbstorage.model.DbConnection;
import atem11.dbstorage.storage.ConnectionStorage;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import java.sql.SQLException;

public class DbConnectionStorage implements ConnectionStorage {
    private static final String VALIDATION_QUERY_SQL = "select 1";
    private static final String MIGRATIONS_LOCATION = "classpath:db/migrations";
    private final ComboPooledDataSource dataSource;

    public DbConnectionStorage(DbConfig dbConfig) {
        var dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(dbConfig.connectionURL());
        dataSource.setUser(dbConfig.username());
        dataSource.setPassword(dbConfig.password());
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(dbConfig.maxPoolSize());
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setPreferredTestQuery(VALIDATION_QUERY_SQL);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(MIGRATIONS_LOCATION)
                .load();
        flyway.migrate();
        this.dataSource = dataSource;
    }

    @Override
    public void saveConnection(String alias, DbConnection dbConnection) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("INSERT INTO connections (connection_alias, connection_url, username, db_password) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING")
        ) {
            int index = 0;
            st.setString(++index, alias);
            st.setString(++index, dbConnection.connectionURL());
            st.setString(++index, dbConnection.username());
            st.setString(++index, dbConnection.password());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeConnection(String alias) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM connections WHERE connection_alias = ?")
        ) {
            int index = 0;
            st.setString(++index, alias);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DbConnection getConnection(String alias) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT connection_url, username, db_password FROM connections WHERE connection_alias = ?")
        ) {
            int index = 0;
            st.setString(++index, alias);
            var rs = st.executeQuery();
            if (rs.next()) {
                return new DbConnection(
                        rs.getString("connection_url"),
                        rs.getString("username"),
                        rs.getString("db_password")
                );
            } else {
                throw new RuntimeException("Unknown db alias");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
