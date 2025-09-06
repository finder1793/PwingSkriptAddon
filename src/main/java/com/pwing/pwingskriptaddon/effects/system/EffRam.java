package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRam extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get ram information";
    }

    @Override
    protected void execute(Event e) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        System.out.println("RAM Info - Used: " + (usedMemory / 1024 / 1024) + "MB, " +
                          "Free: " + (freeMemory / 1024 / 1024) + "MB, " +
                          "Total: " + (totalMemory / 1024 / 1024) + "MB, " +
                          "Max: " + (maxMemory / 1024 / 1024) + "MB");
    }
}
