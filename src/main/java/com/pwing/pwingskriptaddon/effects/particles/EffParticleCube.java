package com.pwing.pwingskriptaddon.effects.particles;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CubeEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleCube extends Effect {

    private Expression<Location> location;
    private Expression<Number> size;
    private Expression<String> particleType;
    private Expression<Number> iterations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        size = (Expression<Number>) exprs[1];
        particleType = (Expression<String>) exprs[2];
        iterations = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle cube";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        Number sizeNum = size.getSingle(e);
        String partStr = particleType.getSingle(e);
        Number iterNum = iterations != null ? iterations.getSingle(e) : null;

        if (loc == null || sizeNum == null || partStr == null) return;

        try {
            Particle particle = Particle.valueOf(partStr.toUpperCase());
            EffectManager em = PwingSkriptAddon.getEffectManager();
            CubeEffect effect = new CubeEffect(em);
            effect.setLocation(loc);
            effect.edgeLength = sizeNum.floatValue();
            effect.particle = particle;
            if (iterNum != null) {
                effect.iterations = iterNum.intValue();
            }
            em.start(effect);
        } catch (IllegalArgumentException ex) {
            // Invalid particle type
        }
    }
}
