package atem11.dbstorage.k8s;

import atem11.dbstorage.client.ExecutorClient;
import atem11.dbstorage.model.DbConnection;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class K8sExecutor {

    private static final String POD_TEMPLATE_PATH = "pod.yaml";
    private static final String POD_PREFIX = "db-executor";
    private static final String NAMESPACE = "dbstorage";
    private final KubernetesClient client;
    private final ServiceConfig serviceConfig;

    public K8sExecutor(
            ClusterDescription description,
            ServiceConfig serviceConfig
    ) {
        client = build(description);
        this.serviceConfig = serviceConfig;
    }

    public void createContainerAndTestConnection(DbConnection connection) {
        var podName = POD_PREFIX + UUID.randomUUID().toString().substring(0, 5);
        var executorPod = podBuilder(podName, client);
        try {
            executorPod = client.pods()
                    .inNamespace(NAMESPACE)
                    .resource(executorPod)
                    .create();
        } catch (Exception e) {
            if (e instanceof KubernetesClientException && ((KubernetesClientException) e).getCode() == HttpURLConnection.HTTP_CONFLICT) {
                throw new RuntimeException("Failed to create executor pod: pod already exist");
            }
            throw new RuntimeException("Failed to create executor pod: " + e.getMessage(), e);
        }
        try (var executor = new ExecutorClient(new URI(executorPod.getStatus().getPodIP() + ":8080"))) {
            try {
                executor.test(connection);
            } finally {
                executor.exit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createContainerAndExecute(DbConnection connection, String statement) {
        var podName = POD_PREFIX + UUID.randomUUID().toString().substring(0, 5);
        var executorPod = podBuilder(podName, client);
        try {
            executorPod = client.pods()
                    .inNamespace(NAMESPACE)
                    .resource(executorPod)
                    .create();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create executor pod: " + e.getMessage(), e);
        }
        String result;
        try (var executor = new ExecutorClient(new URI(executorPod.getStatus().getPodIP() + ":8080"))) {
            try {
                result = executor.execute(connection, statement);
            } finally {
                executor.exit();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Pod podBuilder(String podName, KubernetesClient client) {
        var pod = loadPodTemplate(client);

        final var name = podName.replaceAll("[^-a-z0-9]", "-");
        pod.getMetadata().setName(name);
        pod.getSpec().setAutomountServiceAccountToken(false);
        var container = new Container();
        container.setImage(serviceConfig.getExecutorImage());
        container.setName(POD_PREFIX);
        container.setPorts(List.of(
                new ContainerPortBuilder()
                        .withContainerPort(8080)
                        .withHostPort(8080)
                        .build())
        );

        return pod;
    }

    private Pod loadPodTemplate(KubernetesClient client) {
        try (final var stream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(POD_TEMPLATE_PATH))) {
            return client.pods().load(stream).get();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading pod template " + POD_TEMPLATE_PATH, e);
        }
    }

    private KubernetesClient build(ClusterDescription credentials) {
        final var config = new ConfigBuilder()
                .withMasterUrl(credentials.getMasterAddress())
                .withCaCertData(credentials.getMasterCert())
                .withOauthToken(credentials.getToken())
                .withRequestRetryBackoffInterval(500)
                .withRequestRetryBackoffLimit(10)
                .build();
        return new KubernetesClientBuilder()
                .withConfig(config)
                .build();
    }
}
