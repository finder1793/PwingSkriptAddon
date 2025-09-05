package com.pwing.pwingskriptaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.pwingskriptaddon.events.*;

import com.pwing.pwingskriptaddon.effects.*;
import com.pwing.pwingskriptaddon.conditions.*;
import com.pwing.pwingskriptaddon.effects.file.*;
import com.pwing.pwingskriptaddon.effects.EffPasteSchematic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.PluginCommand;

import java.util.logging.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pwing.pwingskriptaddon.conditions.CondHasPermission;
import com.pwing.pwingskriptaddon.biome.BiomeBrushCommand;
import com.pwing.pwingskriptaddon.biome.BiomeBrushListener;

import de.slikey.effectlib.EffectManager;

public class PwingSkriptAddon extends JavaPlugin {
    private static PwingSkriptAddon instance;
    private SkriptAddon addon;
    private static final Logger logger = Logger.getLogger("PwingSkriptAddon");
    private static final Map<String, CronEvent> cronEvents = new ConcurrentHashMap<>();
    private static EffectManager effectManager;

    @Override
    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);
        effectManager = new EffectManager(this);
        
        try {
            // Load classes from packages
            addon.loadClasses("com.pwing.pwingskriptaddon", "events", "effects", "conditions");
            
            // Register CronTriggerEvent with Bukkit
            getServer().getPluginManager().registerEvents(new CronTriggerEvent(), this);
            
            // Register events
            registerEvents();
            
            // Register effects
            registerEffects();
            
            // Register conditions
            registerConditions();

            // Register biome brush feature if enabled and FAWE present
            registerBiomeBrushIfAvailable();
            
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
        
        // Paste Schematic
        Skript.registerEffect(EffPasteSchematic.class, "paste schematic %string% at %location%");
        
        // World Management Effects
        Skript.registerEffect(EffChangeWeather.class, "change weather in world %string% to %string%");
        Skript.registerEffect(EffSetTime.class, "set time in world %string% to %number%");
        Skript.registerEffect(EffFindStructure.class, "find structure %string% near x %number%, z %number% in world %string%");

        // Spawn Particle
        Skript.registerEffect(EffSpawnParticle.class, "spawn %number% %string% particle[s] at %location% [with data %number%]");

        // Play Sound
        Skript.registerEffect(EffPlaySound.class, "play sound %string% at %location% for %players% [with volume %number%] [pitch %number%]");

        // Particle Circle
        Skript.registerEffect(EffParticleCircle.class, "create particle circle at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Spiral
        Skript.registerEffect(EffParticleSpiral.class, "create particle vortex at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Star
        Skript.registerEffect(EffParticleStar.class, "create particle star at %location% using %string% particle [for %number% iterations]");

        // Particle Arc
        Skript.registerEffect(EffParticleArc.class, "create particle arc at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cone
        Skript.registerEffect(EffParticleCone.class, "create particle cone at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cube
        Skript.registerEffect(EffParticleCube.class, "create particle cube at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Sphere
        Skript.registerEffect(EffParticleSphere.class, "create particle sphere at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cylinder
        Skript.registerEffect(EffParticleCylinder.class, "create particle cylinder at %location% with radius %number% height %number% using %string% particle [for %number% iterations]");

        // Particle Pyramid
        Skript.registerEffect(EffParticlePyramid.class, "create particle pyramid at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Donut
        Skript.registerEffect(EffParticleDonut.class, "create particle donut at %location% with radius %number% tube %number% using %string% particle [for %number% iterations]");

        // Particle Heart
        Skript.registerEffect(EffParticleHeart.class, "create particle heart at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Flame
        Skript.registerEffect(EffParticleFlame.class, "create particle flame at %location% with particles %number% using %string% particle [for %number% iterations]");

        // Particle Wave
        Skript.registerEffect(EffParticleWave.class, "create particle wave at %location% with height %number% width %number% using %string% particle [for %number% iterations]");

        // Particle Love
        Skript.registerEffect(EffParticleLove.class, "create particle love at %location% using %string% particle [for %number% iterations]");

        // Particle Smoke
        Skript.registerEffect(EffParticleSmoke.class, "create particle smoke at %location% with particles %number% using %string% particle [for %number% iterations]");

        // Particle Fountain
        Skript.registerEffect(EffParticleFountain.class, "create particle fountain at %location% with radius %number% height %number% using %string% particle [for %number% iterations]");

        // Particle Explode
        Skript.registerEffect(EffParticleExplode.class, "create particle explode at %location% using %string% particle");

        // Particle Dragon
        Skript.registerEffect(EffParticleDragon.class, "create particle dragon at %location% with length %number% using %string% particle [for %number% iterations]");

        // Particle DiscoBall
        Skript.registerEffect(EffParticleDiscoBall.class, "create particle discoball at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Bleed
        Skript.registerEffect(EffParticleBleed.class, "create particle bleed at %location% with height %number% using %string% particle [for %number% iterations]");

        // Particle Cloud
        Skript.registerEffect(EffParticleCloud.class, "create particle cloud at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Atom
        Skript.registerEffect(EffParticleAtom.class, "create particle atom at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Shield
        Skript.registerEffect(EffParticleShield.class, "create particle shield at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Grid
        Skript.registerEffect(EffParticleGrid.class, "create particle grid at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Music
        Skript.registerEffect(EffParticleMusic.class, "create particle music at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Tornado
        Skript.registerEffect(EffParticleTornado.class, "create particle tornado at %location% with height %number% radius %number% using %string% particle [for %number% iterations]");

        // Particle Dna
        Skript.registerEffect(EffParticleDna.class, "create particle dna at %location% with radius %number% length %number% using %string% particle [for %number% iterations]");

        // Particle Earth
        Skript.registerEffect(EffParticleEarth.class, "create particle earth at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle BigBang
        Skript.registerEffect(EffParticleBigBang.class, "create particle bigbang at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Hill
        Skript.registerEffect(EffParticleHill.class, "create particle hill at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Line
        Skript.registerEffect(EffParticleLine.class, "create particle line at %location% with length %number% using %string% particle [for %number% iterations]");
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
        
        // Placeholder comparison condition
        Skript.registerCondition(CondPlaceholderComparison.class,
            "placeholder %string% (equal|equals|greater than|less than) %number%");
        
        // Has Permission condition
        Skript.registerCondition(CondHasPermission.class,
            "%player% has permission %string%",
            "%player% does(n't| not) have permission %string%");
    }

    private void registerBiomeBrushIfAvailable() {
        saveDefaultConfig();
        boolean enabled = getConfig().getBoolean("biome-brush-enabled", true);
        Plugin fawe = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        if (!enabled || fawe == null || !fawe.isEnabled()) {
            logger.info("BiomeBrush disabled: enabled=" + enabled + ", FAWE=" + (fawe != null && fawe.isEnabled()));
            return;
        }

        // Register command
        PluginCommand cmd = getCommand("biomebrush");
        if (cmd != null) {
            BiomeBrushCommand executor = new BiomeBrushCommand();
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        } else {
            logger.warning("Command 'biomebrush' not found in plugin.yml");
        }

        // Register listener
        getServer().getPluginManager().registerEvents(new BiomeBrushListener(), this);
        logger.info("BiomeBrush feature enabled (FAWE detected).");
    }

    @Override
    public void onDisable() {
        // Shutdown all cron schedulers
        cronEvents.clear();
        if (effectManager != null) effectManager.dispose();
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

    public static EffectManager getEffectManager() {
        return effectManager;
    }
}
