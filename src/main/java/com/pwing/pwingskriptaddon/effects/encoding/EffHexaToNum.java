package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffHexaToNum extends Effect {

    private Expression<String> hexString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        hexString = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert hexadecimal " + hexString.toString(e, debug) + " to number";
    }

    @Override
    protected void execute(Event e) {
        String hex = hexString.getSingle(e);
        if (hex != null) {
            try {
                int decimal = Integer.parseInt(hex.replace("#", ""), 16);
                System.out.println("Decimal: " + decimal);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid hexadecimal string");
            }
        }
    }
}
