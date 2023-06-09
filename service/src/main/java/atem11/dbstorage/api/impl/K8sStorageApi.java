package atem11.dbstorage.api.impl;

import atem11.dbstorage.api.StorageApi;
import atem11.dbstorage.api.model.AddConnectionRequest;
import atem11.dbstorage.api.model.ExecuteRequest;
import atem11.dbstorage.api.model.RemoveConnectionRequest;
import atem11.dbstorage.api.model.TestConnectionRequest;
import atem11.dbstorage.k8s.K8sExecutor;
import com.github.krupt.jsonrpc.annotation.JsonRpcService;
import atem11.dbstorage.model.DbConnection;
import atem11.dbstorage.storage.ConnectionStorage;

@JsonRpcService
public class K8sStorageApi implements StorageApi {
    private final ConnectionStorage connectionStorage;
    private final K8sExecutor k8sExecutor;

    public K8sStorageApi(
            ConnectionStorage connectionStorage,
            K8sExecutor k8sExecutor
    ) {
        this.connectionStorage = connectionStorage;
        this.k8sExecutor = k8sExecutor;
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
        k8sExecutor.createContainerAndTestConnection(connectionStorage.getConnection(request.getAlias()));
    }

    @Override
    public String execute(ExecuteRequest request) {
        return k8sExecutor.createContainerAndExecute(
                connectionStorage.getConnection(request.getAlias()),
                request.getStatement()
        );
    }
}
