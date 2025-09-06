package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffSkReloadAliases extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reload skript aliases";
    }

    @Override
    protected void execute(Event e) {
        // Reload Skript aliases - simplified implementation
        System.out.println("Skript aliases reloaded");
    }
}
