package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileLines extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get lines of file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            try {
                java.nio.file.Path filePath = java.nio.file.Paths.get(path);
                java.util.List<String> lines = java.nio.file.Files.readAllLines(filePath);
                System.out.println("File has " + lines.size() + " lines");
                for (int i = 0; i < Math.min(5, lines.size()); i++) {
                    System.out.println("Line " + (i + 1) + ": " + lines.get(i));
                }
            } catch (Exception ex) {
                System.out.println("Failed to read file lines: " + ex.getMessage());
            }
        }
    }
}
