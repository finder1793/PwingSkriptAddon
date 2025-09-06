package com.pwing.pwingskriptaddon.effects.minecraft;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EffOpenBook extends Effect {
    
    private Expression<String> title;
    private Expression<String> author;
    private Expression<String[]> pages;
    private Expression<Player> players;
    private final BukkitAudiences adventure;
    private final MiniMessage miniMessage;
    
    public EffOpenBook() {
        this.adventure = BukkitAudiences.create(PwingSkriptAddon.getInstance());
        this.miniMessage = MiniMessage.miniMessage();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        title = (Expression<String>) exprs[0];
        author = (Expression<String>) exprs[1];
        pages = (Expression<String[]>) exprs[2];
        players = (Expression<Player>) exprs[3];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "open book to players";
    }

    @Override
    protected void execute(Event e) {
        String bookTitle = title.getSingle(e);
        String bookAuthor = author.getSingle(e);
        String[] bookPages = pages.getSingle(e);
        
        if (bookTitle == null || bookAuthor == null || bookPages == null || players == null) return;

        Component titleComponent = miniMessage.deserialize(bookTitle);
        Component authorComponent = miniMessage.deserialize(bookAuthor);
        
        Book book = Book.builder()
            .title(titleComponent)
            .author(authorComponent)
            .pages(Stream.of(bookPages)
                .map(page -> miniMessage.deserialize(page))
                .collect(Collectors.toList()))
            .build();
        
        for (Player player : players.getArray(e)) {
            adventure.player(player).openBook(book);
        }
    }
}
