package com.pwing.pwingskriptaddon.effects.minecraft;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffPlaySound extends Effect {

    private Expression<String> soundType;
    private Expression<Location> location;
    private Expression<Player> players;
    private Expression<Number> volume;
    private Expression<Number> pitch;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        soundType = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        players = (Expression<Player>) exprs[2];
        volume = (Expression<Number>) exprs[3];
        pitch = (Expression<Number>) exprs[4];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play sound for players";
    }

    @Override
    protected void execute(Event e) {
        String soundStr = soundType.getSingle(e);
        Location loc = location.getSingle(e);
        Player[] playerArray = players.getArray(e);
        Number volVal = volume != null ? volume.getSingle(e) : 1.0f;
        Number pitchVal = pitch != null ? pitch.getSingle(e) : 1.0f;

        if (soundStr == null || loc == null || playerArray == null) return;

        try {
            Sound sound = Sound.valueOf(soundStr.toUpperCase());
            float vol = volVal != null ? volVal.floatValue() : 1.0f;
            float pit = pitchVal != null ? pitchVal.floatValue() : 1.0f;

            for (Player player : playerArray) {
                player.playSound(loc, sound, vol, pit);
            }
        } catch (IllegalArgumentException ex) {
            // Invalid sound type, do nothing
        }
    }
}
