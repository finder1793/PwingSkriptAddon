package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffIsOS extends Effect {

    private Expression<String> osName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        osName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "is operating system " + osName.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String os = osName.getSingle(e);
        if (os != null) {
            String currentOS = System.getProperty("os.name").toLowerCase();
            boolean matches = currentOS.contains(os.toLowerCase());
            System.out.println("OS check '" + os + "': " + matches + " (current: " + currentOS + ")");
        }
    }
}
