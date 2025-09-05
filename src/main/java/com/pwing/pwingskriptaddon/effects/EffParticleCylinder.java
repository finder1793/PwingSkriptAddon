package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Event;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CylinderEffect;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

public class EffParticleCylinder extends Effect {

    private Expression<Location> location;
    private Expression<Number> radius;
    private Expression<Number> height;
    private Expression<String> particleType;
    private Expression<Number> iterations;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        location = (Expression<Location>) exprs[0];
        radius = (Expression<Number>) exprs[1];
        height = (Expression<Number>) exprs[2];
        particleType = (Expression<String>) exprs[3];
        iterations = (Expression<Number>) exprs[4];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create particle cylinder";
    }

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        Number radNum = radius.getSingle(e);
        Number heightNum = height.getSingle(e);
        String partStr = particleType.getSingle(e);
        Number iterNum = iterations != null ? iterations.getSingle(e) : null;

        if (loc == null || radNum == null || heightNum == null || partStr == null) return;

        try {
            Particle particle = Particle.valueOf(partStr.toUpperCase());
            EffectManager em = PwingSkriptAddon.getEffectManager();
            CylinderEffect effect = new CylinderEffect(em);
            effect.setLocation(loc);
            effect.radius = radNum.floatValue();
            effect.height = heightNum.floatValue();
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
