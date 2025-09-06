package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffMorse extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "encode " + text.toString(e, debug) + " to morse";
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            // Simple Morse code mapping for demonstration
            String morse = input.toUpperCase()
                .replace("A", ".- ")
                .replace("B", "-... ")
                .replace("C", "-.-. ")
                .replace("D", "-.. ")
                .replace("E", ". ")
                .replace("F", "..-. ")
                .replace("G", "--. ")
                .replace("H", ".... ")
                .replace("I", ".. ")
                .replace("J", ".--- ")
                .replace("K", "-.- ")
                .replace("L", ".-.. ")
                .replace("M", "-- ")
                .replace("N", "-. ")
                .replace("O", "--- ")
                .replace("P", ".--. ")
                .replace("Q", "--.- ")
                .replace("R", ".-. ")
                .replace("S", "... ")
                .replace("T", "- ")
                .replace("U", "..- ")
                .replace("V", "...- ")
                .replace("W", ".-- ")
                .replace("X", "-..- ")
                .replace("Y", "-.-- ")
                .replace("Z", "--.. ");
            System.out.println("Morse: " + morse.trim());
        }
    }
}
