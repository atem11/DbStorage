package api;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/storage")
public interface StorageApi {
    void addConnection(
            @JsonRpcParam(value = "alias") String alias,
            @JsonRpcParam(value = "connectionString") String connectionString
    );

    void removeConnection(
            @JsonRpcParam(value = "alias") String alias
    );

    void testConnection(
            @JsonRpcParam(value = "alias") String alias
    );

    String execute(
            @JsonRpcParam(value = "alias") String alias,
            @JsonRpcParam(value = "statement") String statement
    );
}
