package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFromUnicode extends Effect {

    private Expression<String> unicodeString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        unicodeString = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert from unicode " + unicodeString.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String unicode = unicodeString.getSingle(e);
        if (unicode != null) {
            try {
                // Simple Unicode decoding - in real implementation handle various formats
                if (unicode.startsWith("U+") || unicode.startsWith("u+")) {
                    int codePoint = Integer.parseInt(unicode.substring(2), 16);
                    String decoded = new String(Character.toChars(codePoint));
                    System.out.println("Decoded from Unicode: " + decoded);
                } else {
                    System.out.println("Invalid Unicode format");
                }
            } catch (Exception ex) {
                System.out.println("Failed to decode Unicode: " + ex.getMessage());
            }
        }
    }
}
