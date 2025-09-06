package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;

public class EffDirList extends Effect {

    private Expression<String> directoryPath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        directoryPath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "list contents of directory " + directoryPath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = directoryPath.getSingle(e);
        if (path != null) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                String[] contents = dir.list();
                if (contents != null) {
                    System.out.println("Directory contents: " + String.join(", ", contents));
                }
            }
        }
    }
}
