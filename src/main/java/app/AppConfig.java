package app;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import storage.ConnectionStorage;

@Configuration
@ComponentScan("storage")
public class AppConfig {

    private final ApplicationContext context;

    public AppConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        return new AutoJsonRpcServiceImplExporter();
    }

    @Bean
    public ConnectionStorage connectionStorage(
            @Value("${connectionStorage.class}") String qualifier
    ) {
        return (ConnectionStorage) context.getBean(qualifier);
    }

}
