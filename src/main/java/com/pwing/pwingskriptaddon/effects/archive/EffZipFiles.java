package com.pwing.pwingskriptaddon.effects.archive;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffZipFiles extends Effect {

    private Expression<String> files;
    private Expression<String> zipFilePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        files = (Expression<String>) exprs[0];
        zipFilePath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "zip files " + files.toString(e, debug) + " to " + zipFilePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String[] fileArray = files.getArray(e);
        String zipPath = zipFilePath.getSingle(e);
        if (fileArray != null && zipPath != null) {
            try {
                java.nio.file.Path zipFile = java.nio.file.Paths.get(zipPath);

                try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(
                        java.nio.file.Files.newOutputStream(zipFile))) {

                    for (String filePath : fileArray) {
                        java.nio.file.Path sourcePath = java.nio.file.Paths.get(filePath.trim());

                        if (!java.nio.file.Files.exists(sourcePath)) {
                            System.out.println("File does not exist, skipping: " + filePath);
                            continue;
                        }

                        String entryName = sourcePath.getFileName().toString();

                        if (java.nio.file.Files.isDirectory(sourcePath)) {
                            // Handle directory zipping
                            java.nio.file.Files.walk(sourcePath).forEach(path -> {
                                try {
                                    if (path.equals(sourcePath)) return;

                                    String relativePath = sourcePath.relativize(path).toString();
                                    String fullEntryName = entryName + "/" + relativePath;

                                    if (java.nio.file.Files.isDirectory(path)) {
                                        if (!fullEntryName.endsWith("/")) {
                                            fullEntryName += "/";
                                        }
                                        zos.putNextEntry(new java.util.zip.ZipEntry(fullEntryName));
                                        zos.closeEntry();
                                    } else {
                                        zos.putNextEntry(new java.util.zip.ZipEntry(fullEntryName));
                                        java.nio.file.Files.copy(path, zos);
                                        zos.closeEntry();
                                    }
                                } catch (Exception ex) {
                                    System.err.println("Error zipping file: " + ex.getMessage());
                                }
                            });
                        } else {
                            // Handle single file zipping
                            zos.putNextEntry(new java.util.zip.ZipEntry(entryName));
                            java.nio.file.Files.copy(sourcePath, zos);
                            zos.closeEntry();
                        }
                    }
                }

                System.out.println("Successfully zipped " + fileArray.length + " files to: " + zipPath);
            } catch (Exception ex) {
                System.out.println("Failed to zip files: " + ex.getMessage());
            }
        }
    }
}
