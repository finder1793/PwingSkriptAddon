package com.pwing.pwingskriptaddon.effects.particles;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.BleedEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleBleed extends Effect {

    private Expression<Location> location;
    private Expression<Number> height;
    private Expression<String> particleType;
    private Expression<Number> iterations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        height = (Expression<Number>) exprs[1];
        particleType = (Expression<String>) exprs[2];
        iterations = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle bleed";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        Number heightNum = height.getSingle(e);
        String partStr = particleType.getSingle(e);
        Number iterNum = iterations != null ? iterations.getSingle(e) : null;

        if (loc == null || heightNum == null || partStr == null) return;

        try {
            EffectManager em = PwingSkriptAddon.getEffectManager();
            BleedEffect effect = new BleedEffect(em);
            effect.setLocation(loc);
            effect.height = heightNum.doubleValue();
            if (iterNum != null) {
                effect.iterations = iterNum.intValue();
            }
            em.start(effect);
        } catch (IllegalArgumentException ex) {
            // Invalid particle type
        }
    }
}
