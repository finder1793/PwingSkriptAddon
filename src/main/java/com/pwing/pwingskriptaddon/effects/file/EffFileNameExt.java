package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;

public class EffFileNameExt extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get name and extension of file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            File file = new File(path);
            String name = file.getName();
            int lastDot = name.lastIndexOf('.');
            String fileName = lastDot > 0 ? name.substring(0, lastDot) : name;
            String extension = lastDot > 0 ? name.substring(lastDot + 1) : "";
            System.out.println("File name: " + fileName + ", Extension: " + extension);
        }
    }
}
