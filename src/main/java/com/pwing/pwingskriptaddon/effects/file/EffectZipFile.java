package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EffectZipFile extends BaseFileEffect {
    private Expression<String> sourcePath;
    private Expression<String> destinationPath;

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

        try (FileOutputStream fos = new FileOutputStream(destination);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            Files.walk(source.toPath()).forEach(path -> {
                try {
                    ZipEntry zipEntry = new ZipEntry(source.toPath().relativize(path).toString());
                    zos.putNextEntry(zipEntry);
                    if (!Files.isDirectory(path)) {
                        Files.copy(path, zos);
                    }
                    zos.closeEntry();
                } catch (Exception ex) {
                    logError("zipping file", ex);
                }
            });
        } catch (Exception ex) {
            logError("zipping file", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "zip file " + sourcePath.toString(e, debug) + " to " + destinationPath.toString(e, debug);
    }
}
