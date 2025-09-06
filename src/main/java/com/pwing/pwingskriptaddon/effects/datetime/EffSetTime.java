package com.pwing.pwingskriptaddon.effects.datetime;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

public class EffSetTime extends Effect {

    private Expression<String> worldName;
    private Expression<Number> time;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldName = (Expression<String>) exprs[0];
        time = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String worldNameStr = worldName.getSingle(e);
        Number timeValue = time.getSingle(e);

        if (worldNameStr == null || timeValue == null) {
            return;
        }

        World world = Bukkit.getWorld(worldNameStr);
        if (world == null) {
            return;
        }

        world.setTime(timeValue.longValue());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set time in world " + worldName.toString(e, debug) + " to " + time.toString(e, debug);
    }
}
