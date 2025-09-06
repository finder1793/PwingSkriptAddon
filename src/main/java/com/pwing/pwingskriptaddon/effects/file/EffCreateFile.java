package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;
import java.io.IOException;

public class EffCreateFile extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            try {
                File file = new File(path);
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                if (file.createNewFile()) {
                    System.out.println("File created: " + path);
                } else {
                    System.out.println("File already exists: " + path);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
