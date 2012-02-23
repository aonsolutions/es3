package com.esferalia.es3.demo.client.flowplayer;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;


public class FlowPlayer
    extends Widget
{
    private static final String ID_PREFIX = "$_$tyco-flowplayer$_$";
    private static int count = 0;
    private static Flow flowPlayer;

    private String id;
    private Player player;

    private String cacheUrl;


    public FlowPlayer()
    {
        if (flowPlayer == null) {
            flowPlayer = new Flow();
        }

        this.id = ID_PREFIX + (count++);

        DivElement element = Document.get().createDivElement();
        element.setId(id);
        Document.get().getBody().appendChild(element);
        player = flowPlayer.createPlayer(id);
        element.removeFromParent();

        player.onLoad(new PlayerLoadHandler()
        {
            @Override
            public void onPlayerLoad(PlayerLoadEvent event)
            {
                onPlayerLoaded();
            }
        });
        setElement(element);
    }


    protected Player getPlayer()
    {
        return player;
    }


    @Override
    protected void onLoad()
    {
        super.onLoad();
    }


    void onPlayerLoaded()
    {
        if (cacheUrl != null) {
            getPlayer().play(cacheUrl);
        }
    }


    public void play(String url)
    {
        if (getPlayer().isLoaded()) {
            getPlayer().play(url);
        }
        else {
            cacheUrl = url;
        }
    }
}
