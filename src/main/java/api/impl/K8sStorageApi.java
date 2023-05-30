package api.impl;

import api.StorageApi;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import model.DbConnection;
import org.springframework.stereotype.Service;
import storage.ConnectionStorage;
import storage.DbConnectionManager;

@Service
@AutoJsonRpcServiceImpl
public class K8sStorageApi implements StorageApi {
    private final ConnectionStorage storage;
    private final DbConnectionManager connectionManager;

    public K8sStorageApi(
            ConnectionStorage storage,
            DbConnectionManager connectionManager
    ){
        this.storage = storage;
        this.connectionManager = connectionManager;
    }
    @Override
    public void addConnection(String alias, String connectionURL, String username, String password) {
        storage.saveConnection(alias, new DbConnection(connectionURL, username, password));
    }

    @Override
    public void removeConnection(String alias) {
        storage.removeConnection(alias);
    }

    @Override
    public void testConnection(String alias) {
        storage.getConnection(alias);
    }

    @Override
    public String execute(String alias, String statement) {
        storage.getConnection(alias);
        return null;
    }
}
