package atem11.test.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import atem11.test.model.DbConnection;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionManager {
    private static final String VALIDATION_QUERY_SQL = "select 1";

    public DbConnectionManager() {
    }

    public final Connection connect(DbConnection config) throws SQLException {
        var dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(config.connectionURL());
        dataSource.setUser(config.username());
        dataSource.setPassword(config.password());
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(10);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setPreferredTestQuery(VALIDATION_QUERY_SQL);
        var conn = dataSource.getConnection();
        conn.setAutoCommit(true);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        return conn;
    }

    public final ResultSet execute(DbConnection config, String statement) throws SQLException {
        try (var connection = connect(config);
             var st = connection.prepareStatement(statement)
        ) {
            return st.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
