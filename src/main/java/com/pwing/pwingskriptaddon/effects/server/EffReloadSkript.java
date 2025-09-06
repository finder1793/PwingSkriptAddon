package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;

public class EffReloadSkript extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reload skript";
    }

    @Override
    protected void execute(Event e) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "skript reload");
        System.out.println("Skript reloaded");
    }
}
