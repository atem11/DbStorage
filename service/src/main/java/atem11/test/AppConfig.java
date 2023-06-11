package atem11.test;

import atem11.test.model.DbConfig;
import atem11.test.storage.ConnectionStorage;
import atem11.test.storage.impl.InMemConnectionStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan("atem11.test.*")
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
}
