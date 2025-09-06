package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffCpuCores extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get cpu cores count";
    }

    @Override
    protected void execute(Event e) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("CPU Cores: " + cores);
    }
}
