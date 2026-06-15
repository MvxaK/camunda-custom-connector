package org.cook.extracter.camunda;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class CamundaConnectorProvider implements ConnectorProvider {

    @Override
    public Connector<?> createConnectorInstance() {
        return new CamundaConnector();
    }

    @Override
    public String getConnectorId() {
        return CamundaConnector.connectorId;
    }

}
