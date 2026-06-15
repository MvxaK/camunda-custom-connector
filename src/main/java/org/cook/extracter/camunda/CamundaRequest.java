package org.cook.extracter.camunda;

import lombok.extern.slf4j.Slf4j;
import org.camunda.connect.impl.AbstractConnectorRequest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CamundaRequest extends AbstractConnectorRequest<CamundaResponse> {

    private final CamundaConnector connector;
    private String url;
    private String method = "GET";
    private final Map<String, String> headers = new HashMap<>();
    private String payload;

    public CamundaRequest(CamundaConnector connector) {
        super(connector);
        this.connector = connector;
    }

    @Override
    public CamundaResponse execute() {
            String url = getRequestParameter("url");
            String method = getRequestParameter("method");
            String payload = getRequestParameter("payload");

            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("Url cannot be empty");
            }

        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url.trim()))
                    .timeout(Duration.ofSeconds(30));

            requestBuilder.header("Content-type", "application/json");
            requestBuilder.header("Accept", "application/json");

            if(CamundaConnector.camundaKey != null){
                requestBuilder.header("Camunda-Header", CamundaConnector.camundaKey);
            }

            String currentMethod = method != null ? method.toUpperCase().trim() : "GET";

            if (currentMethod.equals("POST") || currentMethod.equals("PATCH") || currentMethod.equals("PUT")) {
                String body = payload != null ? payload : "";

                requestBuilder.method(currentMethod, HttpRequest.BodyPublishers.ofString(body));
            } else {
                requestBuilder.method(currentMethod, HttpRequest.BodyPublishers.noBody());
            }

            HttpResponse<String> httpResponse = connector.getHttpClient().send(requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString());

            return new CamundaResponse(httpResponse.statusCode(), httpResponse.body());
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("HTTP request execution interrupted for URL: {}", url, e);

            throw new RuntimeException("Connector execution interrupted", e);
        } catch (Exception e) {
            log.error("Custom Connector failed to execute HTTP request to URL: [{}], Method: [{}]", url, method, e);

            throw new RuntimeException("Custom Connector failed to execute Http request", e);
        }
    }
}
