package atem11.test.api;


import atem11.test.api.model.AddConnectionRequest;
import atem11.test.api.model.ExecuteRequest;
import atem11.test.api.model.RemoveConnectionRequest;
import atem11.test.api.model.TestConnectionRequest;

public interface StorageApi {
    void addConnection(AddConnectionRequest request);

    void removeConnection(RemoveConnectionRequest request);

    void testConnection(TestConnectionRequest request);

    String execute(ExecuteRequest request);
}
