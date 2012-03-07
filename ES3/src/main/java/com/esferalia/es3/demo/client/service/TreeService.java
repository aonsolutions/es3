package com.esferalia.es3.demo.client.service;

import com.esferalia.es3.demo.client.tree.CustomNode;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("tree")
public interface TreeService extends RemoteService {
	
	CustomNode getTree(String name) throws IllegalArgumentException;

//	void changeFileDirectory(File file);
}
