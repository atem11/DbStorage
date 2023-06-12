package atem11.dbstorage.client;

import atem11.dbstorage.model.DbConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ExecutorClient implements AutoCloseable {
    public static final String SERVICE = "/executor";
    public static final String TEST = SERVICE + "/test";
    public static final String EXECUTE = SERVICE + "/execute";
    public static final String EXIT = SERVICE + "/exit";


    private final URI containerUri;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient client;

    public ExecutorClient(
            URI containerUri
    ) {
        this.containerUri = containerUri;
        this.objectMapper = new ObjectMapper();
        client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .setDefaultCookieStore(new BasicCookieStore())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }

    public void test(DbConnection config) {
        try {
            final HttpPost testRequest = new HttpPost(URI.create(containerUri + TEST));
            testRequest.setHeader("Content-Type", "application/json");
            testRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(config)));
            var response = client.execute(testRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String execute(DbConnection config, String statement) {
        try {
            final HttpPost executeRequest = new HttpPost(URI.create(containerUri + EXECUTE));
            executeRequest.setHeader("Content-Type", "application/json");
            executeRequest.setEntity(new StringEntity(objectMapper.writeValueAsString(config)));
            executeRequest.setEntity(new StringEntity(statement));
            var response = client.execute(executeRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException();
            } else {
                return IOUtils.toString(response.getEntity().getContent(), String.valueOf(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit() {
        try {
            final HttpPost exitRequest = new HttpPost(URI.create(containerUri + EXIT));
            exitRequest.setHeader("Content-Type", "application/json");
            var response = client.execute(exitRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void close() throws Exception {
        client.close();
    }
}
