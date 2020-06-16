package net.mcskirmish.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkirmishEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    public SkirmishEvent(boolean async) {
        super(async);
    }

    public SkirmishEvent() {
        this(false);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public static void callEvent(Event event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public final HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
