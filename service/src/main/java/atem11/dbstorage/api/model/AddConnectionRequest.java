package atem11.dbstorage.api.model;

import lombok.Data;

@Data
public class AddConnectionRequest {
    private String alias;
    private String connectionURL;
    private String username;
    private String password;
}