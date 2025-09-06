package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFromBin extends Effect {

    private Expression<String> binaryString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        binaryString = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert from binary " + binaryString.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String binary = binaryString.getSingle(e);
        if (binary != null) {
            try {
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < binary.length(); i += 8) {
                    String byteStr = binary.substring(i, Math.min(i + 8, binary.length()));
                    int charCode = Integer.parseInt(byteStr, 2);
                    text.append((char) charCode);
                }
                System.out.println("Decoded from binary: " + text.toString());
            } catch (Exception ex) {
                System.out.println("Failed to decode binary: " + ex.getMessage());
            }
        }
    }
}
