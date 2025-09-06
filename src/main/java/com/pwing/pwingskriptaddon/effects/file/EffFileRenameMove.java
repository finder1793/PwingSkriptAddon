package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileRenameMove extends Effect {

    private Expression<String> sourceFile;
    private Expression<String> destinationFile;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        sourceFile = (Expression<String>) exprs[0];
        destinationFile = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "rename file " + sourceFile.toString(e, debug) + " to " + destinationFile.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String source = sourceFile.getSingle(e);
        String destination = destinationFile.getSingle(e);
        if (source != null && destination != null) {
            try {
                java.nio.file.Files.move(java.nio.file.Paths.get(source), java.nio.file.Paths.get(destination));
                System.out.println("File renamed/moved from " + source + " to " + destination);
            } catch (Exception ex) {
                System.out.println("Failed to rename/move file: " + ex.getMessage());
            }
        }
    }
}
