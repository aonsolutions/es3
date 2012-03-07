package com.esferalia.es3.demo.client.flowplayer;

import com.google.gwt.core.client.GWT;


public class Flow
{
    private static final String SWF_URL =
            GWT.getModuleBaseURL() + "flowplayer-3.2.7.swf";


    public Flow()
    {
        if (!isWrapperLoaded()) {
            throw new IllegalStateException("JavaScript library not loaded");
        }
    }


    public native Player createPlayer(String id)
    /*-{
        return $wnd.$f(id, @com.esferalia.es3.demo.client.flowplayer.Flow::SWF_URL);
    }-*/;


    public native Player createPlayer(String id, String video)
    /*-{
        return $wnd.$f(id, @com.esferalia.es3.demo.client.flowplayer.Flow::SWF_URL, video);
    }-*/;


    public native Player getPlayer()
    /*-{
        return $wnd.$f();
    }-*/;


    public native Player getPlayer(String id)
    /*-{
        return $wnd.$f(id);
    }-*/;


    private native boolean isWrapperLoaded()
    /*-{
        return typeof($wnd.$f) == 'function';
    }-*/;
}
