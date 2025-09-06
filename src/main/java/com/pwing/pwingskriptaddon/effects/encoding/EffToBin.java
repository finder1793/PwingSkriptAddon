package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffToBin extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert " + text.toString(e, debug) + " to binary";
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            StringBuilder binary = new StringBuilder();
            for (char c : input.toCharArray()) {
                binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
            }
            System.out.println("Binary: " + binary.toString());
        }
    }
}
