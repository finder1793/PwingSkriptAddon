package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUrlLines extends Effect {

    private Expression<String> url;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get lines from url " + url.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String urlStr = url.getSingle(e);
        if (urlStr != null) {
            try {
                java.net.URL urlObj = new java.net.URL(urlStr);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(connection.getInputStream()));
                String line;
                int lineCount = 0;
                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    if (lineCount <= 5) { // Show first 5 lines
                        System.out.println("Line " + lineCount + ": " + line);
                    }
                }
                reader.close();
                System.out.println("Total lines: " + lineCount);
            } catch (Exception ex) {
                System.out.println("Failed to get URL lines: " + ex.getMessage());
            }
        }
    }
}
