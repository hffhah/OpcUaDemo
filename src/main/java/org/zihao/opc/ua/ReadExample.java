package org.zihao.opc.ua;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zihao.opc.ua.constant.Constant;

public class ReadExample implements ClientExample {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String identifier = Constant.READ_TAG;

	public static void main(String[] args) throws Exception {
		ReadExample example = new ReadExample();

		new ClientExampleRunner(example).run();
	}

	@Override
	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
		client.connect().get();
		NodeId nodeId = new NodeId(2, identifier);

		CompletableFuture<DataValue> readValue = client.readValue(0.0, TimestampsToReturn.Both, nodeId);

		logger.info(readValue.get().getValue() + "");

		future.complete(client);
	}
}
