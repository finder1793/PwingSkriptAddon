package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffStartsEndsWith extends Effect {

    private Expression<String> text1;
    private Expression<String> text2;
    private boolean startsWith;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text1 = (Expression<String>) exprs[0];
        text2 = (Expression<String>) exprs[1];
        startsWith = matchedPattern == 0; // Pattern 0 = starts with, Pattern 1 = ends with
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return text1.toString(e, debug) + (startsWith ? " starts with " : " ends with ") + text2.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String str1 = text1.getSingle(e);
        String str2 = text2.getSingle(e);
        if (str1 != null && str2 != null) {
            boolean result = startsWith ? str1.startsWith(str2) : str1.endsWith(str2);
            System.out.println("String comparison result: " + result);
        }
    }
}
