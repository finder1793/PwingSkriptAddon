package com.pwing.pwingskriptaddon.effects.archive;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUnzip extends Effect {

    private Expression<String> zipFilePath;
    private Expression<String> destinationPath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        zipFilePath = (Expression<String>) exprs[0];
        destinationPath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "unzip file " + zipFilePath.toString(e, debug) + " to " + destinationPath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String zipPath = zipFilePath.getSingle(e);
        String destPath = destinationPath.getSingle(e);
        if (zipPath != null && destPath != null) {
            try {
                java.nio.file.Path destDir = java.nio.file.Paths.get(destPath);
                if (!java.nio.file.Files.exists(destDir)) {
                    java.nio.file.Files.createDirectories(destDir);
                }

                try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(
                        java.nio.file.Files.newInputStream(java.nio.file.Paths.get(zipPath)))) {

                    java.util.zip.ZipEntry entry;
                    while ((entry = zis.getNextEntry()) != null) {
                        java.nio.file.Path entryPath = destDir.resolve(entry.getName());

                        if (entry.isDirectory()) {
                            java.nio.file.Files.createDirectories(entryPath);
                        } else {
                            java.nio.file.Files.createDirectories(entryPath.getParent());
                            try (java.io.OutputStream os = java.nio.file.Files.newOutputStream(entryPath)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    os.write(buffer, 0, len);
                                }
                            }
                        }
                        zis.closeEntry();
                    }
                }

                System.out.println("Successfully unzipped to: " + destPath);
            } catch (Exception ex) {
                System.out.println("Failed to unzip file: " + ex.getMessage());
            }
        }
    }
}
