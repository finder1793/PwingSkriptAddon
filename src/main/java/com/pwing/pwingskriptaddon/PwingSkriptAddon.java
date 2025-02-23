package com.pwing.pwingskriptaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.pwingskriptaddon.events.CronEvent;
import com.pwing.pwingskriptaddon.events.CronTriggerEvent;
import com.pwing.pwingskriptaddon.effects.EffSendJsonMessage;
import com.pwing.pwingskriptaddon.effects.EffOpenBook;
import com.pwing.pwingskriptaddon.conditions.*;
import com.pwing.pwingskriptaddon.effects.file.*;
import org.bukkit.Bukkit;

import java.util.logging.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pwing.pwingskriptaddon.effects.file.EffectZipFile;
import com.pwing.pwingskriptaddon.effects.file.EffectUnzipFile;
import com.pwing.pwingskriptaddon.effects.file.EffectSftpTransfer;

public class PwingSkriptAddon extends JavaPlugin {
    private static PwingSkriptAddon instance;
    private SkriptAddon addon;
    private static final Logger logger = Logger.getLogger("PwingSkriptAddon");
    private static final Map<String, CronEvent> cronEvents = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        
        try {
            // Load classes from packages
            addon.loadClasses("com.pwing.pwingskriptaddon", "events", "effects");
            
            // Register CronTriggerEvent with Bukkit
            getServer().getPluginManager().registerEvents(new CronTriggerEvent(), this);
            
            // Register events
            registerEvents();
            
            // Register effects
            registerEffects();
            
            registerConditions(); // Add this line after registerEffects()
            
            logger.info("PwingSkriptAddon has been enabled!");
        } catch (Exception e) {
            logger.severe("Error loading PwingSkriptAddon: " + e.getMessage());
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerEvents() {
        try {
            // Register the Skript event pattern
            Skript.registerEvent("cron", CronEvent.class, CronTriggerEvent.class, 
                "cron %string% start")
                .description("Triggered based on a cron expression")
                .examples("cron \"0 */5 * * * ?\" start:", 
                         "cron \"0 0 4 * * ?\" start:")
                .since("1.0.0");
            
            logger.info("Successfully registered Cron events!");
        } catch (Exception e) {
            logger.severe("Failed to register Cron event pattern: " + e.getMessage());
            e.printStackTrace();
        }
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
        
        // Zip and Unzip
        Skript.registerEffect(EffectZipFile.class, "zip file %string% to %string%");
        Skript.registerEffect(EffectUnzipFile.class, "unzip file %string% to %string%");
        
        // SFTP Transfer
        Skript.registerEffect(EffectSftpTransfer.class, "sftp transfer file %string% to %string% on host %string% with user %string% and password %string%");
    }

    private void registerConditions() {
        // JSON condition
        Skript.registerCondition(CondIsValidJson.class,
            "%string% is valid json",
            "%string% is(n't| not) valid json");
            
        // Directory condition
        Skript.registerCondition(CondIsEmptyDirectory.class,
            "directory %string% is empty",
            "directory %string% is(n't| not) empty");
            
        // Permission group condition
        Skript.registerCondition(CondHasPermissionGroup.class,
            "%player% (is|has) [in] [permission] group %string%",
            "%player% (isn't|is not|hasn't|has not) [in] [permission] group %string%");
    }

    @Override
    public void onDisable() {
        // Shutdown all cron schedulers
        cronEvents.clear();
        logger.info("PwingSkriptAddon has been disabled!");
    }

    public static void registerCronEvent(String expression, CronEvent event) {
        cronEvents.put(expression, event);
        logger.info("[PwingSkriptAddon] Registered cron event: " + expression);
    }

    public static CronEvent getCronEvent(String expression) {
        return cronEvents.get(expression);
    }

    public static PwingSkriptAddon getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return logger;
    }
}
