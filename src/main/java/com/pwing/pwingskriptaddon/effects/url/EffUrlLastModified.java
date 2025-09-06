package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUrlLastModified extends Effect {

    private Expression<String> url;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get last modified date of url " + url.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String urlStr = url.getSingle(e);
        if (urlStr != null) {
            try {
                java.net.URL urlObj = new java.net.URL(urlStr);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();
                long lastModified = connection.getLastModified();
                if (lastModified > 0) {
                    java.util.Date date = new java.util.Date(lastModified);
                    System.out.println("URL last modified: " + date.toString());
                } else {
                    System.out.println("Last modified date not available");
                }
            } catch (Exception ex) {
                System.out.println("Failed to get last modified date: " + ex.getMessage());
            }
        }
    }
}
