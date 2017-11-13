package org.zihao.opc.ua;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zihao.opc.ua.constant.Constant;

public class BrowseNodeExample implements ClientExample {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String nodeBrowser = Constant.READ_NODE_TAG;

	public static void main(String[] args) throws Exception {
		BrowseNodeExample example = new BrowseNodeExample();

		new ClientExampleRunner(example).run();
	}

	@Override
	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
		client.connect().get();

		NodeId nodeId = new NodeId(2, nodeBrowser);
		browseNode("", client, nodeId);

		future.complete(client);
	}

	private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
		try {
			List<Node> nodes = client.getAddressSpace().browse(browseRoot).get();

			for (Node node : nodes) {
				logger.info("{} Node={}", indent, node.getBrowseName().get().getName());

				// recursively browse to children
				browseNode(indent + "  ", client, node.getNodeId().get());
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
		}
	}

}
