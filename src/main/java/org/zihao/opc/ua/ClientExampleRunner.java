
package org.zihao.opc.ua;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientExampleRunner {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();

	private final ClientExample clientExample;

	private final String URL = Constant.URL;

	public ClientExampleRunner(ClientExample clientExample) throws Exception {
		this.clientExample = clientExample;
	}

	private OpcUaClient createClient() throws Exception {
		SecurityPolicy securityPolicy = clientExample.getSecurityPolicy();
		IdentityProvider identityProvider = clientExample.getIdentityProvider();

		EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(URL).get();

		EndpointDescription endpoint = Arrays.stream(endpoints)
				.filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getSecurityPolicyUri())).findFirst()
				.orElseThrow(() -> new Exception("no desired endpoints returned"));

		logger.info("Using endpoint: {} [{}]", endpoint.getEndpointUrl(), securityPolicy);

		OpcUaClientConfig config = OpcUaClientConfig.builder()
				.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
				.setApplicationUri("urn:eclipse:milo:examples:client")
				.setEndpoint(endpoint)
				.setIdentityProvider(identityProvider)
				.setRequestTimeout(uint(5000)).build();

		return new OpcUaClient(config);
	}

	public void run() {
		future.whenComplete((client, ex) -> {
			if (client != null) {
				try {
					client.disconnect().get();
					Stack.releaseSharedResources();
				} catch (InterruptedException | ExecutionException e) {
					logger.error("Error disconnecting:", e.getMessage(), e);
				}
			} else {
				logger.error("Error running example: {}", ex.getMessage(), ex);
				Stack.releaseSharedResources();
			}

			try {
				Thread.sleep(1000);
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		try {
			OpcUaClient client = createClient();

			try {
				clientExample.run(client, future);
				future.get(10, TimeUnit.SECONDS);
			} catch (Throwable t) {
				logger.error("Error running client example: {}", t.getMessage(), t);
				future.complete(client);
			}
		} catch (Throwable t) {
			future.completeExceptionally(t);
		}

		try {
			Thread.sleep(999999999);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
