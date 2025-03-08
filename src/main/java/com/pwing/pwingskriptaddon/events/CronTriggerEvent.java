package com.pwing.pwingskriptaddon.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class CronTriggerEvent extends Event implements Listener {
    private static final HandlerList handlers = new HandlerList();

    public CronTriggerEvent() {
        super(true); 
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}