package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.WaveEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleWave extends Effect {

    private Expression<Location> location;
    private Expression<Number> height;
    private Expression<Number> width;
    private Expression<String> particleType;
    private Expression<Number> iterations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        height = (Expression<Number>) exprs[1];
        width = (Expression<Number>) exprs[2];
        particleType = (Expression<String>) exprs[3];
        iterations = (Expression<Number>) exprs[4];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle wave";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        Number heightNum = height.getSingle(e);
        Number widthNum = width.getSingle(e);
        String partStr = particleType.getSingle(e);
        Number iterNum = iterations != null ? iterations.getSingle(e) : null;

        if (loc == null || heightNum == null || widthNum == null || partStr == null) return;

        try {
            Particle particle = Particle.valueOf(partStr.toUpperCase());
            EffectManager em = PwingSkriptAddon.getEffectManager();
            WaveEffect effect = new WaveEffect(em);
            effect.setLocation(loc);
            effect.height = heightNum.floatValue();
            effect.width = widthNum.floatValue();
            effect.mainParticle = particle;
            if (iterNum != null) {
                effect.iterations = iterNum.intValue();
            }
            em.start(effect);
        } catch (IllegalArgumentException ex) {
            // Invalid particle type
        }
    }
}
