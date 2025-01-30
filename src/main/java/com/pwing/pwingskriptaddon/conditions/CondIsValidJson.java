package com.pwing.pwingskriptaddon.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.StringReader;

public class CondIsValidJson extends Condition {
    
    private Expression<String> json;
    private static final JsonParser parser = new JsonParser();

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        json = (Expression<String>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        String jsonString = json.getSingle(e);
        if (jsonString == null) return false;
        
        try {
            parser.parse(new StringReader(jsonString));
            return !isNegated();
        } catch (JsonSyntaxException ex) {
            return isNegated();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "is valid json " + json.toString(e, debug);
    }
}
