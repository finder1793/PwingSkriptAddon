package com.pwing.pwingskriptaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.pwingskriptaddon.events.*;
import com.pwing.pwingskriptaddon.conditions.*;

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
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectCopyDirectory.class, "copy directory %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectCopyFile.class, "copy file %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectDeleteDirectory.class, "delete directory %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectDeleteFile.class, "delete file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffDirList.class, "list contents of directory %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileNameExt.class, "get name and extension of file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffSFileContents.class, "get contents of file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffCreateFile.class, "create file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileWipe.class, "wipe file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileCreation.class, "get creation time of file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileDeletion.class, "get deletion time of file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileLines.class, "get lines of file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffIsSymbolic.class, "%string% is a symbolic link");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffIsExecutable.class, "%string% is executable");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffWriteFile.class, "write %string% to file %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffZipFiles.class, "zip files %strings% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileExists.class, "file %string% exists");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileDirSize.class, "get size of directory %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffFileMove.class, "move file %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffZipDirectory.class, "zip directory %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectEditYaml.class, 
            "edit yaml %string% set %string% to %string%", 
            "edit yaml %string% set %string% to %string% at line %number%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.file.EffectRenameFileOrDirectory.class, "rename (file|directory) %string% to %string%");

        // URL Operations
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlContents.class, "get contents of url %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlReadLine.class, "read line %number% from url %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlResponseCode.class, "get response code of url %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlOnlineState.class, "url %string% is online");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlLastModified.class, "get last modified date of url %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlLines.class, "get lines from url %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.url.EffUrlSize.class, "get size of url %string%");
        
        // JSON Message
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.json.EffSendJsonMessage.class, "send json message %string% [with hover text %-string%] [(and|with) click command %-string%] to %players%");
        
        // Book Effect
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.minecraft.EffOpenBook.class, 
            "open book titled %string% by %string% with page[s] %strings% to %players%",
            "open book titled %string% by %string% with page %string% to %players%");
        
        // Archive Operations
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffZipDirectory.class, "zip directory %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffZipFiles.class, "zip files %strings% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffZipList.class, "zip list %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.archive.EffUnzip.class, "unzip %string% to %string%");
        
        // SFTP Transfer
        // Skript.registerEffect(com.pwing.pwingskriptaddon.effects.misc.EffectSftpTransfer.class, "sftp transfer file %string% to %string% on host %string% with user %string% and password %string%");
        
        // Paste Schematic
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.minecraft.EffPasteSchematic.class, "paste schematic %string% at %location%");
        
        // World Management Effects
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.minecraft.EffChangeWeather.class, "change weather in world %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.datetime.EffSetTime.class, "set time in world %string% to %number%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.minecraft.EffFindStructure.class, "find structure %string% near x %number%, z %number% in world %string%");

        // Spawn Particle
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffSpawnParticle.class, "spawn %number% %string% particle[s] at %location% [with data %number%]");

        // Play Sound
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.minecraft.EffPlaySound.class, "play sound %string% at %location% for %players% [with volume %number%] [pitch %number%]");

        // Particle Circle
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleCircle.class, "create particle circle at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Spiral
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleSpiral.class, "create particle vortex at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Star
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleStar.class, "create particle star at %location% using %string% particle [for %number% iterations]");

        // Particle Arc
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleArc.class, "create particle arc at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cone
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleCone.class, "create particle cone at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cube
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleCube.class, "create particle cube at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Sphere
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleSphere.class, "create particle sphere at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Cylinder
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleCylinder.class, "create particle cylinder at %location% with radius %number% height %number% using %string% particle [for %number% iterations]");

        // Particle Pyramid
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticlePyramid.class, "create particle pyramid at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Donut
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleDonut.class, "create particle donut at %location% with radius %number% tube %number% using %string% particle [for %number% iterations]");

        // Particle Heart
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleHeart.class, "create particle heart at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Flame
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleFlame.class, "create particle flame at %location% with particles %number% using %string% particle [for %number% iterations]");

        // Particle Wave
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleWave.class, "create particle wave at %location% with height %number% width %number% using %string% particle [for %number% iterations]");

        // Particle Love
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleLove.class, "create particle love at %location% using %string% particle [for %number% iterations]");

        // Particle Smoke
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleSmoke.class, "create particle smoke at %location% with particles %number% using %string% particle [for %number% iterations]");

        // Particle Fountain
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleFountain.class, "create particle fountain at %location% with radius %number% height %number% using %string% particle [for %number% iterations]");

        // Particle Explode
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleExplode.class, "create particle explode at %location% using %string% particle");

        // Particle Dragon
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleDragon.class, "create particle dragon at %location% with length %number% using %string% particle [for %number% iterations]");

        // Particle DiscoBall
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleDiscoBall.class, "create particle discoball at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Bleed
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleBleed.class, "create particle bleed at %location% with height %number% using %string% particle [for %number% iterations]");

        // Particle Cloud
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleCloud.class, "create particle cloud at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Atom
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleAtom.class, "create particle atom at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Shield
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleShield.class, "create particle shield at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Grid
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleGrid.class, "create particle grid at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle Music
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleMusic.class, "create particle music at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Tornado
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleTornado.class, "create particle tornado at %location% with height %number% radius %number% using %string% particle [for %number% iterations]");

        // Particle Dna
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleDna.class, "create particle dna at %location% with radius %number% length %number% using %string% particle [for %number% iterations]");

        // Particle Earth
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleEarth.class, "create particle earth at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle BigBang
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleBigBang.class, "create particle bigbang at %location% with radius %number% using %string% particle [for %number% iterations]");

        // Particle Hill
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleHill.class, "create particle hill at %location% with size %number% using %string% particle [for %number% iterations]");

        // Particle line
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.particles.EffParticleLine.class, "create particle line at %location% with radius %number% using %string% particle [for %number% iterations]");

        // String Manipulation Effects
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.encoding.EffNumToHexa.class, "convert %number% to hexadecimal");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.encoding.EffHexToRgb.class, "convert hexadecimal %string% to rgb");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffBase64.class, "encode %string% to base64");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffMorse.class, "encode %string% to morse");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffMirrorText.class, "mirror %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffRandomizeString.class, "randomize %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffStartsEndsWith.class,
            "%string% starts with %string%",
            "%string% ends with %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffToUpperLower.class,
            "convert %string% to uppercase",
            "convert %string% to lowercase");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffClearAccented.class, "clear accented characters from %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.text.EffCaseLength.class, "length of %string%");

        // YAML Operations
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.misc.EffSYaml.class, "set yaml %string% key %string% to %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.misc.EffYamlExists.class, "yaml file %string% exists");

        // System Information
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.system.EffGetTimeZone.class, "get current time zone");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.system.EffCpuCores.class, "get cpu cores count");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.system.EffGetSysProp.class, "get system property %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.system.EffIsOS.class, "is operating system %string%");

        // Server Management
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffReloadSkript.class, "reload skript");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffRestartServer.class, "restart server");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffRunOpCmd.class, "run op command %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffRunCmd.class, "run command %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffRunCode.class, "run code %string%");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffRunCmdOutput.class, "run command %string% and get output");
        Skript.registerEffect(com.pwing.pwingskriptaddon.effects.server.EffSkReloadAliases.class, "reload skript aliases");
    
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
