package com.pwing.pwingskriptaddon.effects.particles;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ExplodeEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleExplode extends Effect {

    private Expression<Location> location;
    private Expression<String> particleType;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        particleType = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle explode";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        String partStr = particleType.getSingle(e);

        if (loc == null || partStr == null) return;

        try {
            Particle particle = Particle.valueOf(partStr.toUpperCase());
            EffectManager em = PwingSkriptAddon.getEffectManager();
            ExplodeEffect effect = new ExplodeEffect(em);
            effect.setLocation(loc);
            effect.particle1 = particle;
            em.start(effect);
        } catch (IllegalArgumentException ex) {
            // Invalid particle type
        }
    }
}
