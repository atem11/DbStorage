package atem11.test;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ParamConverterProvider;
import org.apache.cxf.endpoint.Server;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class AppConfig {

    private final ApplicationContext context;

    public AppConfig(ApplicationContext context) {
        this.context = context;
    }
    @Bean
    public Server restServer(Bus bus) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("");

        List<Object> services = new ArrayList<>();
        services.add(context.getBean(ExecutorApi.class));
        endpoint.setServiceBeans(services);

        Set<Object> allProviders = new HashSet<>();
        allProviders.addAll(context.getBeansOfType(ContextProvider.class).values());
        allProviders.addAll(context.getBeansOfType(MessageBodyWriter.class).values());
        allProviders.addAll(context.getBeansOfType(MessageBodyReader.class).values());
        allProviders.addAll(context.getBeansOfType(ParamConverterProvider.class).values());
        endpoint.setProviders(new ArrayList<>(allProviders));

        return endpoint.create();
    }
}
