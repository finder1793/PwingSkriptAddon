package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LoveEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleLove extends Effect {

    private Expression<Location> location;
    private Expression<String> particleType;
    private Expression<Number> iterations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        particleType = (Expression<String>) exprs[1];
        iterations = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle love";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        String partStr = particleType.getSingle(e);
        Number iterNum = iterations != null ? iterations.getSingle(e) : null;

        if (loc == null || partStr == null) return;

        try {
            Particle particle = Particle.valueOf(partStr.toUpperCase());
            EffectManager em = PwingSkriptAddon.getEffectManager();
            LoveEffect effect = new LoveEffect(em);
            effect.setLocation(loc);
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
