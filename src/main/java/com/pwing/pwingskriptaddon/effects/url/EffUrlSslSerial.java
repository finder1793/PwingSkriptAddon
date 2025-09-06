package com.pwing.pwingskriptaddon.effects.url;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import javax.net.ssl.HttpsURLConnection;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.net.URL;

public class EffUrlSslSerial extends Effect {

    private Expression<String> url;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get ssl serial number of url " + url.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String urlStr = url.getSingle(e);
        if (urlStr != null) {
            try {
                URL urlObj = new URL(urlStr);
                HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
                connection.connect();
                Certificate[] certs = connection.getServerCertificates();
                if (certs.length > 0 && certs[0] instanceof X509Certificate) {
                    X509Certificate cert = (X509Certificate) certs[0];
                    String serial = cert.getSerialNumber().toString();
                    System.out.println("SSL Serial Number: " + serial);
                }
                connection.disconnect();
            } catch (Exception ex) {
                System.out.println("Failed to get SSL serial number: " + ex.getMessage());
            }
        }
    }
}
