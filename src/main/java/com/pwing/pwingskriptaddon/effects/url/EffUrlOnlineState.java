package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUrlOnlineState extends Effect {

    private Expression<String> url;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ch.njol.skript.lang.SkriptParser.ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "url " + url.toString(e, debug) + " is online";
    }

    @Override
    protected void execute(Event e) {
        String urlStr = url.getSingle(e);
        if (urlStr != null) {
            try {
                java.net.URL urlObj = new java.net.URL(urlStr);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) urlObj.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                boolean isOnline = responseCode >= 200 && responseCode < 400;
                System.out.println("URL online check: " + isOnline + " (response: " + responseCode + ")");
            } catch (Exception ex) {
                System.out.println("URL online check failed: " + ex.getMessage());
            }
        }
    }
}
