package atem11.test.api.impl;

import atem11.test.api.StorageApi;
import atem11.test.api.model.AddConnectionRequest;
import atem11.test.api.model.ExecuteRequest;
import atem11.test.api.model.RemoveConnectionRequest;
import atem11.test.api.model.TestConnectionRequest;
import com.github.krupt.jsonrpc.annotation.JsonRpcService;
import atem11.test.model.DbConnection;
import atem11.test.storage.ConnectionStorage;

@JsonRpcService
public class K8sStorageApi implements StorageApi {
    private final ConnectionStorage connectionStorage;

    public K8sStorageApi(
            ConnectionStorage connectionStorage
    ) {
        this.connectionStorage = connectionStorage;
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
        return null;
    }
}
