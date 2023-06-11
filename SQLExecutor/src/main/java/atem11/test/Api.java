package atem11.test;

import atem11.test.executor.DbConnectionManager;
import atem11.test.model.DbConnection;
import org.springframework.stereotype.Service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/executor")
@Service
public class Api {

    private final DbConnectionManager dbConnectionManager;

    public Api(DbConnectionManager dbConnectionManager) {
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
    public ResultSet execute(DbConnection config, String statement) {
        try {
            return dbConnectionManager.execute(config, statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("exit")
    public void exit() {
        System.exit(0);
    }
}
