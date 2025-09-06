package com.pwing.pwingskriptaddon.effects.archive;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

public class EffZipList extends Effect {

    private Expression<String> zipFilePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        zipFilePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "list contents of zip file " + zipFilePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = zipFilePath.getSingle(e);
        if (path != null) {
            try (ZipFile zipFile = new ZipFile(path)) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                System.out.println("ZIP Contents:");
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    System.out.println("- " + entry.getName());
                }
            } catch (Exception ex) {
                System.out.println("Failed to read ZIP file: " + ex.getMessage());
            }
        }
    }
}
