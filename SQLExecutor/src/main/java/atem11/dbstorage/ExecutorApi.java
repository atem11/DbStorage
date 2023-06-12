package atem11.dbstorage;

import atem11.dbstorage.executor.DbConnectionManager;
import atem11.dbstorage.model.DbConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        try (var conn = dbConnectionManager.connect(config)) {
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/execute")
    @Produces(MediaType.APPLICATION_JSON)
    public String execute(DbConnection config, String statement) {
        try {
            var res =  dbConnectionManager.execute(config, statement);
            if (res instanceof ResultSet) {
                var rs = (ResultSet) res;
                ResultSetMetaData md = rs.getMetaData();
                int numCols = md.getColumnCount();
                List<String> colNames = IntStream.range(0, numCols)
                        .mapToObj(i -> {
                            try {
                                return md.getColumnName(i + 1);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                return "?";
                            }
                        })
                        .collect(Collectors.toList());
                JSONArray result = new JSONArray();
                while (rs.next()) {
                    JSONObject row = new JSONObject();
                    colNames.forEach(cn -> {
                        try {
                            row.put(cn, rs.getObject(cn));
                        } catch (JSONException | SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    result.put(row);
                }

                return result.toString();
            } else if (res instanceof Integer) {
                return res.toString();
            } else {
                throw new RuntimeException("Invalid statement");
            }
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
