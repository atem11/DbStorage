package atem11.dbstorage.executor;

import atem11.dbstorage.model.DbConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import static org.junit.Assert.fail;

public class DbConnectionManagerTest {

    private DbConnectionManager connectionManager;
    private final DbConnection testConnection1 = new DbConnection(
            "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false",
            "test",
            ""
    );
    @Before
    public void setUp() {
        connectionManager = new DbConnectionManager();
    }

    @Test
    public void testConnection() {
        try (var conn = connectionManager.connect(testConnection1);
             var st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS test (col1 varchar(25) NOT NULL);")) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConnectionAndExecute() {
        try (var conn = connectionManager.connect(testConnection1);
             var st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS test (col1 varchar(25) NOT NULL);")) {
            st.executeUpdate();
            connectionManager.execute(testConnection1, "INSERT INTO test values ('1')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void executeInvalidStatement() {
        try (var conn = connectionManager.connect(testConnection1);
             var st = conn.prepareStatement("CREATE TABLE IF NOT EXISTS test (col1 varchar(25) NOT NULL);")) {
            st.executeUpdate();
            try {
                connectionManager.execute(testConnection1, "INSERT INTO testik values ('1')");
                fail();
            } catch (Exception ignored) {
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}