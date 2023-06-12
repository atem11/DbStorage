package atem11.dbstorage;

import atem11.dbstorage.k8s.ClusterDescription;
import atem11.dbstorage.k8s.ServiceConfig;
import atem11.dbstorage.model.DbConfig;
import atem11.dbstorage.storage.ConnectionStorage;
import atem11.dbstorage.storage.impl.DbConnectionStorage;
import atem11.dbstorage.storage.impl.InMemConnectionStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan("atem11.dbstorage.*")
public class AppConfig {

    private final ApplicationContext context;

    public AppConfig(ApplicationContext context) {
        this.context = context;
    }

    @Lazy
    @Bean("InMemConnectionStorage")
    public InMemConnectionStorage inMemConnectionStorage() {
        return new InMemConnectionStorage();
    }

    @Lazy
    @Bean("DbConnectionStorage")
    public DbConnectionStorage dbConnectionStorage(DbConfig dbConfig) {
        return new DbConnectionStorage(dbConfig);
    }
    @Bean
    public ConnectionStorage connectionStorage(
            @Value("${connectionStorage.class}") String qualifier
    ) {
        return (ConnectionStorage) context.getBean(qualifier);
    }

    @Lazy
    @Bean
    public DbConfig dbConfig(
            @Value("${db.config.url}") String connectionURL,
            @Value("${db.config.username}") String username,
            @Value("${db.config.password}") String password,
            @Value("${db.config.maxPoolSize}") Integer maxPoolSize
    ) {
        return new DbConfig(connectionURL, username, password, maxPoolSize);
    }

    @Lazy
    @Bean
    public ClusterDescription description(
            @Value("${k8s.config.address}") String masterAddress,
            @Value("${k8s.config.cert}") String masterCert,
            @Value("${k8s.config.token}") String token
    ) {
        var desc = new ClusterDescription();
        desc.setMasterAddress(masterAddress);
        desc.setMasterCert(masterCert);
        desc.setToken(token);
        return desc;
    }

    @Lazy
    @Bean
    public ServiceConfig serviceConfig(
            @Value("${k8s.helm.executor.image}") String image
    ) {
        var config = new ServiceConfig();
        config.setExecutorImage(image);
        return config;
    }
}
