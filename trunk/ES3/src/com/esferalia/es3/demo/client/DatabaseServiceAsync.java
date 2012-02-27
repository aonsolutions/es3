package com.esferalia.es3.demo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DatabaseService</code>.
 */
public interface DatabaseServiceAsync {

	void loadDatabase(AsyncCallback<Void> callback);
}
