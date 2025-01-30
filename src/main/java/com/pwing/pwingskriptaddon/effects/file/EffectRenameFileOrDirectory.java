package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EffectRenameFileOrDirectory extends BaseFileEffect {
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

        try {
            Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            logError("renaming " + (source.isDirectory() ? "directory" : "file"), ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "rename " + sourcePath.toString(e, debug) + " to " + destinationPath.toString(e, debug);
    }
}
