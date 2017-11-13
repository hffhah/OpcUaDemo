package org.zihao.opc.ua;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zihao.opc.ua.constant.Constant;

public class WriteExample implements ClientExample {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String identifier = Constant.WRITE_TAG;

	public static void main(String[] args) throws Exception {
		WriteExample example = new WriteExample();

		new ClientExampleRunner(example).run();
	}

	@Override
	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
		client.connect().get();

		NodeId nodeId = new NodeId(2, identifier);

		Variant v = new Variant(0);

		DataValue dataValue = new DataValue(v, null, null);

		CompletableFuture<StatusCode> f = client.writeValue(nodeId, dataValue);
		StatusCode statusCode = f.get();
		boolean good = statusCode.isGood();
		if (good) {
			logger.info("write success!");

		} else {
			logger.info("write failed!");

		}

		future.complete(client);
	}

}
