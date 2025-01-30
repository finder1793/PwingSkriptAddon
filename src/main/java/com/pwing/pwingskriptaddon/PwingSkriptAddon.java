package com.pwing.pwingskriptaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.pwingskriptaddon.events.CronEvent;
import com.pwing.pwingskriptaddon.events.CronTriggerEvent;
import com.pwing.pwingskriptaddon.effects.EffSendJsonMessage;
import com.pwing.pwingskriptaddon.effects.EffOpenBook;
import com.pwing.pwingskriptaddon.effects.file.*;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class PwingSkriptAddon extends JavaPlugin {
    private static PwingSkriptAddon instance;
    private SkriptAddon addon;
    private static final Logger logger = Logger.getLogger("PwingSkriptAddon");

    @Override
    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        
        try {
            // Load classes from packages
            addon.loadClasses("com.pwing.pwingskriptaddon", "events", "effects");
            
            // Register events
            registerEvents();
            
            // Register effects
            registerEffects();
            
            logger.info("PwingSkriptAddon has been enabled!");
        } catch (Exception e) {
            logger.severe("Error loading PwingSkriptAddon: " + e.getMessage());
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerEvents() {
        // Register with proper event class
        Skript.registerEvent("cron", CronEvent.class, 
            CronTriggerEvent.class,  // Use our custom event class
            "cron %string% start");
    }

    private void registerEffects() {
        // File Operations
        Skript.registerEffect(EffectCopyDirectory.class, "copy directory %string% to %string%");
        Skript.registerEffect(EffectCopyFile.class, "copy file %string% to %string%");
        Skript.registerEffect(EffectDeleteDirectory.class, "delete directory %string%");
        Skript.registerEffect(EffectDeleteFile.class, "delete file %string%");
        Skript.registerEffect(EffectEditYaml.class, 
            "edit yaml %string% set %string% to %string%", 
            "edit yaml %string% set %string% to %string% at line %number%");
        Skript.registerEffect(EffectRenameFileOrDirectory.class, "rename (file|directory) %string% to %string%");
        
        // JSON Message
        Skript.registerEffect(EffSendJsonMessage.class, "send json message %string% [with hover text %-string%] [(and|with) click command %-string%] to %players%");
        
        // Book Effect
        Skript.registerEffect(EffOpenBook.class, 
            "open book titled %string% by %string% with page[s] %strings% to %players%",
            "open book titled %string% by %string% with page %string% to %players%");
    }

    @Override
    public void onDisable() {
        logger.info("PwingSkriptAddon has been disabled!");
    }

    public static PwingSkriptAddon getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return logger;
    }
}
