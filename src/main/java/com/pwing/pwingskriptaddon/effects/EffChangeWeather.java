package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

public class EffChangeWeather extends Effect {

    private Expression<String> worldName;
    private Expression<String> weatherType;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldName = (Expression<String>) exprs[0];
        weatherType = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String worldNameStr = worldName.getSingle(e);
        String weatherTypeStr = weatherType.getSingle(e);

        if (worldNameStr == null || weatherTypeStr == null) {
            return;
        }

        World world = Bukkit.getWorld(worldNameStr);
        if (world == null) {
            return;
        }

        switch (weatherTypeStr.toLowerCase()) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                break;
            case "thunder":
                world.setStorm(true);
                world.setThundering(true);
                break;
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "change weather in world " + worldName.toString(e, debug) + " to " + weatherType.toString(e, debug);
    }
}
