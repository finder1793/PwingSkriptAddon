package com.pwing.pwingskriptaddon.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;

public class CondHasPermissionGroup extends Condition {
    
    private Expression<Player> player;
    private Expression<String> group;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        group = (Expression<String>) exprs[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        Player p = player.getSingle(e);
        String g = group.getSingle(e);
        if (p == null || g == null) return false;
        
        Permission perms = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
        boolean inGroup = perms.playerInGroup(p, g);
        return isNegated() ? !inGroup : inGroup;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return player.toString(e, debug) + " is in group " + group.toString(e, debug);
    }
}
