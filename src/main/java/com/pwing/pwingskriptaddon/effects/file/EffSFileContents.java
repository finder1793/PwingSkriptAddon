package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EffSFileContents extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get contents of file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            try {
                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    String content = new String(Files.readAllBytes(Paths.get(path)));
                    System.out.println("File contents: " + content);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
