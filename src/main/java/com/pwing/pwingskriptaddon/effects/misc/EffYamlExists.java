package com.pwing.pwingskriptaddon.effects.misc;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;

public class EffYamlExists extends Effect {

    private Expression<String> yamlFile;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        yamlFile = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "yaml file " + yamlFile.toString(e, debug) + " exists";
    }

    @Override
    protected void execute(Event e) {
        String file = yamlFile.getSingle(e);
        if (file != null) {
            File yamlFile = new File(file);
            boolean exists = yamlFile.exists() && yamlFile.isFile();
            System.out.println("YAML file exists: " + exists);
        }
    }
}
