package atem11.test.api.model;

import lombok.Data;

@Data
public class ExecuteRequest {
    private String alias;
    private String statement;
}