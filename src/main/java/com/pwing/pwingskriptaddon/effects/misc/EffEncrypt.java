package com.pwing.pwingskriptaddon.effects.misc;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffEncrypt extends Effect {

    private Expression<String> text;
    private Expression<String> algorithm;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        algorithm = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "encrypt " + text.toString(e, debug) + " with algorithm " + algorithm.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        String algo = algorithm.getSingle(e);
        if (input != null && algo != null) {
            try {
                // Simple Caesar cipher as example - in real implementation you'd use proper encryption
                StringBuilder encrypted = new StringBuilder();
                for (char c : input.toCharArray()) {
                    encrypted.append((char) (c + 3)); // Simple shift
                }
                System.out.println("Encrypted: " + encrypted.toString());
            } catch (Exception ex) {
                System.out.println("Encryption failed: " + ex.getMessage());
            }
        }
    }
}
