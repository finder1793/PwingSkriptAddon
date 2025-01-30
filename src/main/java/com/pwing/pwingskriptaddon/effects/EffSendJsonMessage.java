package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class EffSendJsonMessage extends Effect {
    
    private Expression<String> message;
    private Expression<String> hoverText;
    private Expression<String> command;
    private Expression<Player> players;
    private final BukkitAudiences adventure;
    private final MiniMessage miniMessage;
    
    public EffSendJsonMessage() {
        this.adventure = BukkitAudiences.create(PwingSkriptAddon.getInstance());
        this.miniMessage = MiniMessage.miniMessage();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        message = (Expression<String>) exprs[0];
        hoverText = (Expression<String>) exprs[1];
        command = (Expression<String>) exprs[2];
        players = (Expression<Player>) exprs[3];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send json message to players";
    }

    @Override
    protected void execute(Event e) {
        String messageStr = message.getSingle(e);
        String hoverStr = hoverText != null ? hoverText.getSingle(e) : null;
        String commandStr = command != null ? command.getSingle(e) : null;
        
        if (messageStr == null || players == null) return;

        // Convert legacy color codes and parse hex colors
        messageStr = messageStr.replace("§", "&");
        Component component = miniMessage.deserialize(messageStr);

        if (hoverStr != null) {
            hoverStr = hoverStr.replace("§", "&");
            component = component.hoverEvent(HoverEvent.showText(miniMessage.deserialize(hoverStr)));
        }

        if (commandStr != null) {
            if (!commandStr.startsWith("/")) {
                commandStr = "/" + commandStr;
            }
            component = component.clickEvent(ClickEvent.runCommand(commandStr));
        }

        for (Player player : players.getArray(e)) {
            adventure.player(player).sendMessage(component);
        }
    }
}
