package com.pwing.pwingskriptaddon.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CondHasPermission extends Condition {

    private Expression<Player> player;
    private Expression<String> permission;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        permission = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Player p = player.getSingle(e);
        String perm = permission.getSingle(e);
        if (p == null || perm == null) return false;
        return p.hasPermission(perm) != isNegated();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return player.toString(e, debug) + " has permission " + permission.toString(e, debug);
    }
}
