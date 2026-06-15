package org.cook.extracter.camunda;

import lombok.Getter;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorRequestInterceptor;
import org.camunda.connect.spi.ConnectorResponse;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CamundaConnector implements Connector<CamundaRequest> {

    public final static String connectorId = "mva-http-connector";

    public final static String camundaKey = System.getenv("CAMUNDA_SECRET_KEY");

    @Getter
    private final HttpClient httpClient;
    private final List<ConnectorRequestInterceptor> requestInterceptors = new ArrayList<>();

    public CamundaConnector(){
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public String getId() {
        return connectorId;
    }

    @Override
    public ConnectorResponse execute(CamundaRequest camundaRequest) {
        return camundaRequest.execute();
    }

    @Override
    public CamundaRequest createRequest() {
        return new CamundaRequest(this);
    }

    @Override
    public List<ConnectorRequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    @Override
    public void setRequestInterceptors(List<ConnectorRequestInterceptor> list) {
        if(list != null){
            this.requestInterceptors.clear();
            this.requestInterceptors.addAll(list);
        }
    }

    @Override
    public Connector<CamundaRequest> addRequestInterceptor(ConnectorRequestInterceptor connectorRequestInterceptor) {
        if(connectorRequestInterceptor != null){
            this.requestInterceptors.add(connectorRequestInterceptor);
        }

        return this;
    }

    @Override
    public Connector<CamundaRequest> addRequestInterceptors(Collection<ConnectorRequestInterceptor> collection) {
        if(collection != null){
            this.requestInterceptors.addAll(collection);
        }

        return this;
    }
}
