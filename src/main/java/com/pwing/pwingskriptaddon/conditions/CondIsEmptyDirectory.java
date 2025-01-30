package com.pwing.pwingskriptaddon.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.io.File;

public class CondIsEmptyDirectory extends Condition {
    
    private Expression<String> directory;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        directory = (Expression<String>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        String path = directory.getSingle(e);
        if (path == null) return false;
        
        File dir = new File(path);
        if (!dir.isDirectory()) return false;
        
        String[] contents = dir.list();
        boolean isEmpty = contents == null || contents.length == 0;
        return isNegated() ? !isEmpty : isEmpty;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "directory " + directory.toString(e, debug) + " is empty";
    }
}
