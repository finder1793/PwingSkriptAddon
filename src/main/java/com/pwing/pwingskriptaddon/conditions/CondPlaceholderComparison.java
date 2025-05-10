package com.pwing.pwingskriptaddon.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CondPlaceholderComparison extends Condition {

    private Expression<String> placeholder;
    private Expression<String> comparison;
    private Expression<Number> value;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        placeholder = (Expression<String>) exprs[0];
        comparison = (Expression<String>) exprs[1];
        value = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    public boolean check(Event e) {
        String placeholderString = placeholder.getSingle(e);
        String comparisonType = comparison.getSingle(e);
        Number comparisonValue = value.getSingle(e);

        if (placeholderString == null || comparisonType == null || comparisonValue == null) {
            return false;
        }

        Player player = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null); // Example: Use the first online player
        if (player == null) {
            return false;
        }

        String result = PlaceholderAPI.setPlaceholders(player, placeholderString);
        try {
            double placeholderValue = Double.parseDouble(result);
            double targetValue = comparisonValue.doubleValue();

            switch (comparisonType.toLowerCase()) {
                case "equal":
                case "equals":
                    return placeholderValue == targetValue;
                case "greater than":
                    return placeholderValue > targetValue;
                case "less than":
                    return placeholderValue < targetValue;
                default:
                    return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "placeholder " + placeholder.toString(e, debug) + " comparison " + comparison.toString(e, debug) + " value " + value.toString(e, debug);
    }
}
