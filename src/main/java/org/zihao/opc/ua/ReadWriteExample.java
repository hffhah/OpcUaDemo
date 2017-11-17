package org.zihao.opc.ua;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class ReadWriteExample implements ClientExample {

	public static void main(String[] args) throws Exception {
		ReadWriteExample example = new ReadWriteExample();

		new ClientExampleRunner(example).run();
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
		// synchronous connect
		client.connect().get();

		List<NodeId> nodeIds = ImmutableList.of(new NodeId(2, Constant.TAG)); // Channel1.Device1.Tag1

		// read
		CompletableFuture<DataValue> read = client.readValue(0, TimestampsToReturn.Both, nodeIds.get(0));
		logger.info("read=" + read.get().getValue().toString());

		// write
//		DataValue dataValue = new DataValue(new Variant(0), null, null);
		DataValue dataValue = new DataValue(new Variant(0));
		CompletableFuture<StatusCode> writeValue = client.writeValue(nodeIds.get(0), dataValue);
		logger.info("result success =" + writeValue.get().isGood() + ",desc=" + writeValue.get());

		future.complete(client);
	}

}
