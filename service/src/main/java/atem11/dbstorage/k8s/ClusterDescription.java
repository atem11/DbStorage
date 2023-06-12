package atem11.dbstorage.k8s;

import lombok.Data;

@Data
public class ClusterDescription {
    private String masterAddress;
    private String masterCert;
    private String token;
}
