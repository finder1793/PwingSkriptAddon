package com.pwing.pwingskriptaddon.effects.archive;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffZipDirectory extends Effect {

    private Expression<String> directoryPath;
    private Expression<String> zipFilePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        directoryPath = (Expression<String>) exprs[0];
        zipFilePath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "zip directory " + directoryPath.toString(e, debug) + " to " + zipFilePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String dirPath = directoryPath.getSingle(e);
        String zipPath = zipFilePath.getSingle(e);
        if (dirPath != null && zipPath != null) {
            try {
                java.nio.file.Path sourceDir = java.nio.file.Paths.get(dirPath);
                java.nio.file.Path zipFile = java.nio.file.Paths.get(zipPath);

                if (!java.nio.file.Files.exists(sourceDir) || !java.nio.file.Files.isDirectory(sourceDir)) {
                    System.out.println("Source directory does not exist or is not a directory: " + dirPath);
                    return;
                }

                try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(
                        java.nio.file.Files.newOutputStream(zipFile))) {

                    java.nio.file.Files.walk(sourceDir).forEach(path -> {
                        try {
                            // Skip the root directory itself
                            if (path.equals(sourceDir)) return;

                            String entryName = sourceDir.relativize(path).toString();
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
                            System.err.println("Error zipping directory entry: " + ex.getMessage());
                        }
                    });
                }

                System.out.println("Successfully zipped directory to: " + zipPath);
            } catch (Exception ex) {
                System.out.println("Failed to zip directory: " + ex.getMessage());
            }
        }
    }
}
