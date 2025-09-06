package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRunOpCmd extends Effect {

    private Expression<String> command;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        command = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "run op command " + command.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String cmd = command.getSingle(e);
        if (cmd != null) {
            try {
                org.bukkit.Bukkit.getServer().dispatchCommand(
                    org.bukkit.Bukkit.getConsoleSender(), cmd);
                System.out.println("OP Command executed: " + cmd);
            } catch (Exception ex) {
                System.out.println("Failed to run OP command: " + ex.getMessage());
            }
        }
    }
}
