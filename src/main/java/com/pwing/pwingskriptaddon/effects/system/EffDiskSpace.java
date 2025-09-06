package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffDiskSpace extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get disk space";
    }

    @Override
    protected void execute(Event e) {
        java.io.File root = new java.io.File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usableSpace = root.getUsableSpace();

        System.out.println("Disk Space - Total: " + (totalSpace / 1024 / 1024 / 1024) + "GB, " +
                          "Free: " + (freeSpace / 1024 / 1024 / 1024) + "GB, " +
                          "Usable: " + (usableSpace / 1024 / 1024 / 1024) + "GB");
    }
}
