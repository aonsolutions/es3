package com.esferalia.es3.demo.client.flowplayer;

import com.google.gwt.core.client.JavaScriptObject;


public class Clip
    extends JavaScriptObject
{
    protected Clip()
    {
        // Required for JavaScriptObject
    }

    public final native String getCompleteUrl()
    /*-{
        return this.completeUrl;
    }-*/;
}