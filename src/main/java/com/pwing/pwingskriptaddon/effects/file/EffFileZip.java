package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileZip extends Effect {

    private Expression<String> filePath;
    private Expression<String> zipFilePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        zipFilePath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "zip file " + filePath.toString(e, debug) + " to " + zipFilePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String file = filePath.getSingle(e);
        String zipFile = zipFilePath.getSingle(e);
        if (file != null && zipFile != null) {
            try {
                java.nio.file.Path sourcePath = java.nio.file.Paths.get(file);
                java.nio.file.Path zipPath = java.nio.file.Paths.get(zipFile);

                try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(
                        java.nio.file.Files.newOutputStream(zipPath))) {

                    java.nio.file.Files.walk(sourcePath).forEach(path -> {
                        try {
                            String entryName = sourcePath.relativize(path).toString();
                            if (entryName.isEmpty()) return;

                            if (java.nio.file.Files.isDirectory(path)) {
                                if (!entryName.endsWith("/")) {
                                    entryName += "/";
                                }
                                zos.putNextEntry(new java.util.zip.ZipEntry(entryName));
                                zos.closeEntry();
                            } else {
                                zos.putNextEntry(new java.util.zip.ZipEntry(entryName));
                                java.nio.file.Files.copy(path, zos);
                                zos.closeEntry();
                            }
                        } catch (Exception ex) {
                            System.err.println("Error zipping file: " + ex.getMessage());
                        }
                    });
                }

                System.out.println("Successfully zipped file to: " + zipFile);
            } catch (Exception ex) {
                System.out.println("Failed to zip file: " + ex.getMessage());
            }
        }
    }
}
