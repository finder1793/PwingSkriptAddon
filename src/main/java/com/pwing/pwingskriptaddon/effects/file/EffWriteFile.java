package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffWriteFile extends Effect {

    private Expression<String> content;
    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        content = (Expression<String>) exprs[0];
        filePath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "write " + content.toString(e, debug) + " to file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String text = content.getSingle(e);
        String path = filePath.getSingle(e);
        if (text != null && path != null) {
            try {
                java.nio.file.Files.write(java.nio.file.Paths.get(path), text.getBytes());
                System.out.println("Content written to file: " + path);
            } catch (Exception ex) {
                System.out.println("Failed to write to file: " + ex.getMessage());
            }
        }
    }
}
