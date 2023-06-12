package atem11.dbstorage.api.model;

import lombok.Data;

@Data
public class ExecuteRequest {
    private String alias;
    private String statement;
}