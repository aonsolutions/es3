package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.CustomNode;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	CustomNode greetServer(String name) throws IllegalArgumentException;
}
