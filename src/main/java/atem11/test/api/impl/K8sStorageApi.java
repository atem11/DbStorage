package atem11.test.api.impl;

import atem11.test.api.StorageApi;
import atem11.test.api.model.AddConnectionRequest;
import atem11.test.api.model.ExecuteRequest;
import atem11.test.api.model.RemoveConnectionRequest;
import atem11.test.api.model.TestConnectionRequest;
import com.github.krupt.jsonrpc.annotation.JsonRpcService;
import atem11.test.model.DbConnection;
import atem11.test.storage.ConnectionStorage;
import atem11.test.storage.DbConnectionManager;

import java.sql.SQLException;

@JsonRpcService
public class K8sStorageApi implements StorageApi {
    private final ConnectionStorage connectionStorage;
    private final DbConnectionManager dbConnectionManager;

    public K8sStorageApi(
            ConnectionStorage connectionStorage,
            DbConnectionManager dbConnectionManager
    ) {
        this.connectionStorage = connectionStorage;
        this.dbConnectionManager = dbConnectionManager;
        System.out.println("K8sStorageApi running");
    }

    @Override
    public void addConnection(AddConnectionRequest request) {
        connectionStorage.saveConnection(
                request.getAlias(),
                new DbConnection(
                        request.getConnectionURL(),
                        request.getUsername(),
                        request.getPassword()
                )
        );
    }

    @Override
    public void removeConnection(RemoveConnectionRequest request) {
        connectionStorage.removeConnection(request.getAlias());
    }

    @Override
    public void testConnection(TestConnectionRequest request) {
        connectionStorage.getConnection(request.getAlias());
    }

    @Override
    public String execute(ExecuteRequest request) {
        try (var connection = dbConnectionManager.connect(request.getAlias());
             var st = connection.prepareStatement(request.getStatement())
        ) {
            var rs = st.executeQuery();
            return rs.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
