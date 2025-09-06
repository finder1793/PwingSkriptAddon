package com.pwing.pwingskriptaddon.effects.misc;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffHash extends Effect {

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
        return "hash " + text.toString(e, debug) + " with algorithm " + algorithm.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        String algo = algorithm.getSingle(e);
        if (input != null && algo != null) {
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance(algo.toUpperCase());
                byte[] hashBytes = digest.digest(input.getBytes());
                StringBuilder hash = new StringBuilder();
                for (byte b : hashBytes) {
                    hash.append(String.format("%02x", b));
                }
                System.out.println("Hash: " + hash.toString());
            } catch (Exception ex) {
                System.out.println("Failed to hash: " + ex.getMessage());
            }
        }
    }
}
