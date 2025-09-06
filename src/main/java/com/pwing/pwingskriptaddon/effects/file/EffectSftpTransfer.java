package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.bukkit.event.Event;

import java.io.FileInputStream;
import java.util.Properties;

public class EffectSftpTransfer extends BaseFileEffect {
    private Expression<String> localFilePath;
    private Expression<String> remoteFilePath;
    private Expression<String> host;
    private Expression<String> username;
    private Expression<String> password;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        localFilePath = (Expression<String>) exprs[0];
        remoteFilePath = (Expression<String>) exprs[1];
        host = (Expression<String>) exprs[2];
        username = (Expression<String>) exprs[3];
        password = (Expression<String>) exprs[4];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String localPath = localFilePath.getSingle(e);
        String remotePath = remoteFilePath.getSingle(e);
        String sftpHost = host.getSingle(e);
        String user = username.getSingle(e);
        String pass = password.getSingle(e);

        if (localPath == null || remotePath == null || sftpHost == null || user == null || pass == null) {
            logError("Invalid SFTP transfer parameters", null);
            return;
        }

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, sftpHost, 22);
            session.setPassword(pass);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (FileInputStream fis = new FileInputStream(localPath)) {
                channelSftp.put(fis, remotePath);
            }

            channelSftp.disconnect();
            session.disconnect();
        } catch (Exception ex) {
            logError("SFTP transfer failed", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "sftp transfer file " + localFilePath.toString(e, debug) + " to " + remoteFilePath.toString(e, debug) + " on host " + host.toString(e, debug) + " with user " + username.toString(e, debug) + " and password " + password.toString(e, debug);
    }
}
