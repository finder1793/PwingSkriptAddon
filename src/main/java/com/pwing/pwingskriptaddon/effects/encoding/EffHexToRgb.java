package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

public class EffHexToRgb extends Effect {

    private Expression<String> hexColor;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hexColor = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert hexadecimal " + hexColor.toString(e, debug) + " to rgb";
    }

    @Override
    protected void execute(Event e) {
        String hex = hexColor.getSingle(e);
        if (hex != null && hex.matches("#?[0-9A-Fa-f]{6}")) {
            // Remove # if present
            hex = hex.replace("#", "");

            int r = Integer.valueOf(hex.substring(0, 2), 16);
            int g = Integer.valueOf(hex.substring(2, 4), 16);
            int b = Integer.valueOf(hex.substring(4, 6), 16);

            String rgb = r + "," + g + "," + b;
            // Store result in Skript variable
            Variables.setVariable("rgbresult", rgb, e, false);
        }
    }
}
