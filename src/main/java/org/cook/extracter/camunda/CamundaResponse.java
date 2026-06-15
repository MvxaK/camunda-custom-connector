package org.cook.extracter.camunda;

import java.util.HashMap;
import java.util.Map;

import org.camunda.connect.spi.ConnectorResponse;

public class CamundaResponse implements ConnectorResponse {

    private final Integer statusCode;
    private final String responseBody;

    public CamundaResponse(Integer statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getResponseParameter(String name) {
        if ("response".equals(name)) {
            return (V) this.responseBody;
        }
        if ("statusCode".equals(name)) {
            return (V) this.statusCode;
        }

        return null;
    }

    @Override
    public Map<String, Object> getResponseParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("response", responseBody);
        parameters.put("statusCode", statusCode);

        return parameters;
    }

}
