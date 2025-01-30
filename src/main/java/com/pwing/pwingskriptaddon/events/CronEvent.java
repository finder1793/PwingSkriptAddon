package com.pwing.pwingskriptaddon.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.lang.SkriptParser;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.quartz.CronExpression;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CronEvent extends SkriptEvent {
    private String cronExpression;
    private TriggerItem triggerItem;

    // Custom event class for handling the actual event
    public static class CronTriggerEvent extends Event {
        private static final HandlerList handlers = new HandlerList();

        public static HandlerList getHandlerList() {
            return handlers;
        }

        @Override
        public HandlerList getHandlers() {
            return handlers;
        }
    }

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        cronExpression = args[0].toString();
        return cronExpression != null;
    }

    @Override
    public boolean check(Event e) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "cron " + cronExpression + " start";
    }

    public void run(Event e) {
        var scheduler = Executors.newScheduledThreadPool(1);
        try {
            var cron = new CronExpression(cronExpression);
            var nextRun = cron.getNextValidTimeAfter(new Date());
            var delay = nextRun.getTime() - System.currentTimeMillis();
            scheduler.schedule(() -> Bukkit.getScheduler().runTask(PwingSkriptAddon.getInstance(), 
                () -> TriggerItem.walk(getNextTriggerItem(), new CronTriggerEvent())), 
                delay, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TriggerItem getNextTriggerItem() {
        return getNext();
    }

    private TriggerItem getNext() {
        return getTriggerItem().getNext();
    }

    private TriggerItem getTriggerItem() {
        return triggerItem;
    }

    public void setTriggerItem(TriggerItem item) {
        this.triggerItem = item;
    }

    public String[] getSyntaxes() {
        return new String[]{"[on] cron %string% start"};
    }
}
