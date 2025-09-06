package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUrlReadLine extends Effect {

    private Expression<String> url;
    private Expression<Number> lineNumber;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        lineNumber = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "read line " + lineNumber.toString(e, debug) + " from url " + url.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String urlStr = url.getSingle(e);
        Number lineNum = lineNumber.getSingle(e);
        if (urlStr != null && lineNum != null) {
            try {
                java.net.URL urlObj = new java.net.URL(urlStr);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(connection.getInputStream()));

                String line;
                int currentLine = 1;
                while ((line = reader.readLine()) != null) {
                    if (currentLine == lineNum.intValue()) {
                        System.out.println("Line " + lineNum.intValue() + ": " + line);
                        break;
                    }
                    currentLine++;
                }
                reader.close();
            } catch (Exception ex) {
                System.out.println("Failed to read URL line: " + ex.getMessage());
            }
        }
    }
}
