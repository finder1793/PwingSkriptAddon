package com.pwing.pwingskriptaddon.events;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptEventHandler;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.cron.CronExpressionSchedule;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.util.UUID;

public class CronEvent extends SelfRegisteringSkriptEvent {
    private Scheduler scheduler;
    private String schedulerId;
    private Trigger trigger;
    private String cronExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        cronExpression = ((Literal<String>) args[0]).getSingle();
        Bukkit.getLogger().info("[PwingSkriptAddon] Initializing cron task: " + cronExpression);
        return true;
    }

    private void execute() {
        final Trigger trigger = this.trigger;
        if (trigger == null) {
            Bukkit.getLogger().warning("[PwingSkriptAddon] Trigger is null for cron: " + cronExpression);
            return;
        }
        
        Bukkit.getLogger().info("[PwingSkriptAddon] Executing cron task: " + cronExpression);
        final CronTriggerEvent event = new CronTriggerEvent();
        SkriptEventHandler.logEventStart(event);
        SkriptEventHandler.logTriggerStart(trigger);
        trigger.execute(event);
        SkriptEventHandler.logTriggerEnd(trigger);
        SkriptEventHandler.logEventEnd();
        Bukkit.getLogger().info("[PwingSkriptAddon] Completed cron task: " + cronExpression);
    }

    @Override
    public void register(Trigger trigger) {
        this.trigger = trigger;
        scheduler = new Scheduler();
        schedulerId = UUID.randomUUID().toString();
        
        try {
            final CronExpressionSchedule schedule = cronExpression.split(" ").length == 6 
                ? CronExpressionSchedule.parseWithSeconds(cronExpression)
                : CronExpressionSchedule.parse(cronExpression);
                
            Bukkit.getLogger().info("[PwingSkriptAddon] Registering cron task:");
            Bukkit.getLogger().info("[PwingSkriptAddon] Expression: " + cronExpression);
            Bukkit.getLogger().info("[PwingSkriptAddon] ID: " + schedulerId);
            
            scheduler.schedule(schedulerId, () -> execute(), schedule);
            Bukkit.getLogger().info("[PwingSkriptAddon] Successfully scheduled cron task!");
        } catch (Exception e) {
            Bukkit.getLogger().severe("[PwingSkriptAddon] Failed to schedule cron task: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(Trigger trigger) {
        Bukkit.getLogger().info("[PwingSkriptAddon] Unregistering cron task: " + cronExpression);
        assert trigger == this.trigger;
        this.trigger = null;
        unregisterAll();
    }

    @Override
    public void unregisterAll() {
        if (scheduler != null && schedulerId != null) {
            Bukkit.getLogger().info("[PwingSkriptAddon] Shutting down cron task: " + cronExpression);
            scheduler.cancel(schedulerId);
            scheduler = null;
            schedulerId = null;
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "cron " + cronExpression + " start";
    }
}
