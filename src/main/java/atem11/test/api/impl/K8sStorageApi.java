package atem11.test.api.impl;

import atem11.test.api.StorageApi;
import com.github.krupt.jsonrpc.annotation.JsonRpcService;
import atem11.test.model.DbConnection;
import atem11.test.storage.ConnectionStorage;
import atem11.test.storage.DbConnectionManager;

@JsonRpcService
public class K8sStorageApi implements StorageApi {
    private final ConnectionStorage connectionStorage;
    private final DbConnectionManager dbConnectionManager;

    public K8sStorageApi(
            ConnectionStorage connectionStorage,
            DbConnectionManager dbConnectionManager
    ){
        this.connectionStorage = connectionStorage;
        this.dbConnectionManager = dbConnectionManager;
        System.out.println("K8sStorageApi running");
    }
    @Override
    public void addConnection(String alias, String connectionURL, String username, String password) {
        connectionStorage.saveConnection(alias, new DbConnection(connectionURL, username, password));
    }

    @Override
    public void removeConnection(String alias) {
        connectionStorage.removeConnection(alias);
    }

    @Override
    public void testConnection(String alias) {
        connectionStorage.getConnection(alias);
    }

    @Override
    public String execute(String alias, String statement) {
        connectionStorage.getConnection(alias);
        return null;
    }
}
