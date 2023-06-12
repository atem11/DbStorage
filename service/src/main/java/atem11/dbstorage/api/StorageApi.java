package atem11.dbstorage.api;


import atem11.dbstorage.api.model.AddConnectionRequest;
import atem11.dbstorage.api.model.ExecuteRequest;
import atem11.dbstorage.api.model.RemoveConnectionRequest;
import atem11.dbstorage.api.model.TestConnectionRequest;

public interface StorageApi {
    void addConnection(AddConnectionRequest request);

    void removeConnection(RemoveConnectionRequest request);

    void testConnection(TestConnectionRequest request);

    String execute(ExecuteRequest request);
}
