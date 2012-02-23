package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.tree.CustomNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<CustomNode> callback)
			throws IllegalArgumentException;
}
