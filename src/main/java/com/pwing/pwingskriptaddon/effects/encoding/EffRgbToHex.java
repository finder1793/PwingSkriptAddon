package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRgbToHex extends Effect {

    private Expression<String> rgbString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        rgbString = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert rgb " + rgbString.toString(e, debug) + " to hex";
    }

    @Override
    protected void execute(Event e) {
        String rgb = rgbString.getSingle(e);
        if (rgb != null) {
            try {
                String[] parts = rgb.split(",");
                if (parts.length == 3) {
                    int r = Integer.parseInt(parts[0].trim());
                    int g = Integer.parseInt(parts[1].trim());
                    int b = Integer.parseInt(parts[2].trim());
                    String hex = String.format("#%02X%02X%02X", r, g, b);
                    System.out.println("Hex: " + hex);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid RGB format");
            }
        }
    }
}
