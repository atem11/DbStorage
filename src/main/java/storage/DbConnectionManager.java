package storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionManager {
    private static final String VALIDATION_QUERY_SQL = "select 1";

    private final Map<String, ComboPooledDataSource> dataSources;
    private final ConnectionStorage storage;

    public DbConnectionManager(
            ConnectionStorage storage
    ) {
        this.dataSources = new HashMap<>();
        this.storage = storage;
    }

    public final Connection connect(String alias) throws SQLException {
        var connPool = dataSources.get(alias);
        if (connPool != null) {
            var conn = connPool.getConnection();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            return conn;
        } else {
            var dataSource = new ComboPooledDataSource();
            var config = storage.getConnection(alias);
            dataSource.setJdbcUrl(config.connectionURL());
            dataSource.setUser(config.username());
            dataSource.setPassword(config.password());
            dataSource.setMinPoolSize(1);
            dataSource.setMaxPoolSize(100); // TODO need startUP configuration
            dataSource.setTestConnectionOnCheckout(true);
            dataSource.setPreferredTestQuery(VALIDATION_QUERY_SQL);

            dataSources.put(alias, dataSource);
            return connect(alias);
        }
    }

    @PreDestroy
    public void close() {
        dataSources.forEach((s, comboPooledDataSource) -> comboPooledDataSource.close());
    }
}
