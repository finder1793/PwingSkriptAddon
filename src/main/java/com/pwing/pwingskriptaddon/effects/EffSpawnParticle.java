package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;

public class EffSpawnParticle extends Effect {

    private Expression<Number> count;
    private Expression<String> particleType;
    private Expression<Location> location;
    private Expression<Number> data;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        count = (Expression<Number>) exprs[0];
        particleType = (Expression<String>) exprs[1];
        location = (Expression<Location>) exprs[2];
        data = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "spawn particles";
    }

    @Override
    protected void execute(Event e) {
        Number countVal = count.getSingle(e);
        String typeStr = particleType.getSingle(e);
        Location loc = location.getSingle(e);
        Number dataVal = data != null ? data.getSingle(e) : null;

        if (countVal == null || typeStr == null || loc == null) return;

        try {
            Particle particle = Particle.valueOf(typeStr.toUpperCase());
            int particleCount = countVal.intValue();
            if (dataVal != null) {
                loc.getWorld().spawnParticle(particle, loc, particleCount, dataVal.doubleValue());
            } else {
                loc.getWorld().spawnParticle(particle, loc, particleCount);
            }
        } catch (IllegalArgumentException ex) {
            // Invalid particle type, do nothing
        }
    }
}
