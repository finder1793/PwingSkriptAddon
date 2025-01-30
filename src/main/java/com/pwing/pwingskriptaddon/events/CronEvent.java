package com.pwing.pwingskriptaddon.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.SkriptParser;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;
import org.quartz.CronExpression;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CronEvent extends SkriptEvent {
    private String cronExpression;
    private TriggerItem triggerItem;

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        cronExpression = args[0].toString();
        if (cronExpression != null) {
            Bukkit.getLogger().info("[PwingSkriptAddon] Loading cron task with expression: " + cronExpression);
            try {
                // Add helper message for common mistakes
                if (!cronExpression.contains("?")) {
                    Bukkit.getLogger().warning("[PwingSkriptAddon] Cron expression must contain a '?' for day-of-week or day-of-month");
                    Bukkit.getLogger().warning("[PwingSkriptAddon] Example: \"0 0 4 * * ?\" for daily at 4 AM");
                    return false;
                }
                
                if (cronExpression.split(" ").length < 6) {
                    Bukkit.getLogger().warning("[PwingSkriptAddon] Cron expression must have 6 parts: seconds minutes hours day-of-month month day-of-week");
                    Bukkit.getLogger().warning("[PwingSkriptAddon] Example: \"0 0 4 * * ?\" for daily at 4 AM");
                    return false;
                }

                // Validate cron expression
                new CronExpression(cronExpression);
                Bukkit.getLogger().info("[PwingSkriptAddon] Successfully validated cron expression: " + cronExpression);
                return true;
            } catch (Exception ex) {
                Bukkit.getLogger().severe("[PwingSkriptAddon] Invalid cron expression: " + cronExpression);
                Bukkit.getLogger().severe("[PwingSkriptAddon] Error: " + ex.getMessage());
                Bukkit.getLogger().severe("[PwingSkriptAddon] Format: seconds minutes hours day-of-month month day-of-week");
                Bukkit.getLogger().severe("[PwingSkriptAddon] Note: Either day-of-week OR day-of-month must be '?'");
                Bukkit.getLogger().severe("[PwingSkriptAddon] Example: \"0 0 4 * * ?\" for daily at 4 AM");
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean check(Event e) {
        return e instanceof CronTriggerEvent;
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
            
            // Enhanced logging
            Bukkit.getLogger().info("----------------------------------------");
            Bukkit.getLogger().info("[PwingSkriptAddon] Scheduling new cron task:");
            Bukkit.getLogger().info("[PwingSkriptAddon] Expression: " + cronExpression);
            Bukkit.getLogger().info("[PwingSkriptAddon] Next execution: " + nextRun);
            Bukkit.getLogger().info("[PwingSkriptAddon] Delay: " + delay + "ms");
            Bukkit.getLogger().info("----------------------------------------");
            
            scheduler.schedule(() -> Bukkit.getScheduler().runTask(PwingSkriptAddon.getInstance(), 
                () -> {
                    Bukkit.getLogger().info("[PwingSkriptAddon] Executing cron task: " + cronExpression);
                    TriggerItem.walk(getNextTriggerItem(), new CronTriggerEvent());
                    // Schedule next execution
                    run(e);
                }), 
                delay, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            Bukkit.getLogger().severe("[PwingSkriptAddon] Failed to schedule cron task: " + cronExpression);
            Bukkit.getLogger().severe("[PwingSkriptAddon] Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private TriggerItem getNextTriggerItem() {
        return getTriggerItem().getNext();
    }

    private TriggerItem getTriggerItem() {
        return triggerItem;
    }

    public void setTriggerItem(TriggerItem item) {
        this.triggerItem = item;
    }
}
