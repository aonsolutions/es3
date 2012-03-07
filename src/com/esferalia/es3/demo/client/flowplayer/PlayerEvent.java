package com.esferalia.es3.demo.client.flowplayer;

abstract class PlayerEvent
{
    private Player player;


    public PlayerEvent(Player player)
    {
        this.player = player;
    }


    public Player getPlayer()
    {
        return player;
    }
}
