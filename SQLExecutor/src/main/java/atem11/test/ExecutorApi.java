package atem11.test;

import atem11.test.executor.DbConnectionManager;
import atem11.test.model.DbConnection;
import org.springframework.stereotype.Service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Service
@Path("/executor")
public class ExecutorApi {

    private final DbConnectionManager dbConnectionManager;

    public ExecutorApi(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    @POST
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public void test(DbConnection config) {
        try {
            dbConnectionManager.connect(config);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/execute")
    @Produces(MediaType.APPLICATION_JSON)
    public String execute(DbConnection config, String statement) {
        try (var rs =  dbConnectionManager.execute(config, statement)) {
            var builder = new StringBuilder();
            while (rs.next()) {
                builder.append(rs);
            }
            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/exit")
    public void exit() {
        System.exit(0);
    }
}
