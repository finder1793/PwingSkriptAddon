package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUrlSize extends Effect {

    private Expression<String> url;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get size of url " + url.toString(e, debug);
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

                int contentLength = connection.getContentLength();
                if (contentLength > 0) {
                    System.out.println("URL size: " + (contentLength / 1024) + " KB");
                } else {
                    System.out.println("Content length not available");
                }
            } catch (Exception ex) {
                System.out.println("Failed to get URL size: " + ex.getMessage());
            }
        }
    }
}
