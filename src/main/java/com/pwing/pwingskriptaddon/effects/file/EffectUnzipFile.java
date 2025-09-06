package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EffectUnzipFile extends BaseFileEffect {
    private Expression<String> sourcePath;
    private Expression<String> destinationPath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        sourcePath = (Expression<String>) exprs[0];
        destinationPath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        File source = new File(sourcePath.getSingle(e));
        File destination = new File(destinationPath.getSingle(e));

        if (!validateFile(source, true)) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(source);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(destination, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newFile.toPath());
                } else {
                    Files.createDirectories(newFile.getParentFile().toPath());
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        Files.copy(zis, newFile.toPath());
                    }
                }
                zis.closeEntry();
            }
        } catch (Exception ex) {
            logError("unzipping file", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "unzip file " + sourcePath.toString(e, debug) + " to " + destinationPath.toString(e, debug);
    }
}
