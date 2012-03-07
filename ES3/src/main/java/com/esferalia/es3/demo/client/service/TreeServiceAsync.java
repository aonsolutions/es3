package com.esferalia.es3.demo.client.service;

import com.esferalia.es3.demo.client.tree.CustomNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TreeServiceAsync {
	
	void getTree(String input, AsyncCallback<CustomNode> callback)
			throws IllegalArgumentException;
	
//	void changeFileDirectory(File file, AsyncCallback<Void> callback);
}
