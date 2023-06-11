package atem11.test.api.model;

import lombok.Data;

@Data
public class AddConnectionRequest {
    private String alias;
    private String connectionURL;
    private String username;
    private String password;
}