package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRunCmdOutput extends Effect {

    private Expression<String> command;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        command = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "run command " + command.toString(e, debug) + " and get output";
    }

    @Override
    protected void execute(Event e) {
        String cmd = command.getSingle(e);
        if (cmd != null) {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                process.waitFor();
                System.out.println("Command output: " + output.toString());
            } catch (Exception ex) {
                System.out.println("Failed to run command: " + ex.getMessage());
            }
        }
    }
}
