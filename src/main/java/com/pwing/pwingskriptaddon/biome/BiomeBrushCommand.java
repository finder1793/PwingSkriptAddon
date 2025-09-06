package com.pwing.pwingskriptaddon.biome;

import com.pwing.pwingskriptaddon.PwingSkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BiomeBrushCommand implements CommandExecutor, TabCompleter {
    public static final String PERM = "pwingskriptaddon.biomebrush";
    public static final NamespacedKey KEY_BIOME = new NamespacedKey(PwingSkriptAddon.getInstance(), "biomebrush_biome");
    public static final NamespacedKey KEY_RADIUS = new NamespacedKey(PwingSkriptAddon.getInstance(), "biomebrush_radius");
    public static final NamespacedKey KEY_WORLD = new NamespacedKey(PwingSkriptAddon.getInstance(), "biomebrush_world");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(PERM)) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " give <biome> [radius] [world]");
            return true;
        }

        String biomeName = args[1].toUpperCase(Locale.ROOT);
        Biome biome;
        try {
            biome = Biome.valueOf(biomeName);
        } catch (IllegalArgumentException ex) {
            player.sendMessage(ChatColor.RED + "Unknown biome: " + args[1]);
            return true;
        }

        int radius = 5;
        if (args.length >= 3) {
            try {
                radius = Math.max(1, Math.min(32, Integer.parseInt(args[2])));
            } catch (NumberFormatException ex) {
                player.sendMessage(ChatColor.RED + "Radius must be a number (1-32). Using default 5.");
                radius = 5;
            }
        }

        World world = player.getWorld();
        if (args.length >= 4) {
            String worldName = args[3];
            World targetWorld = Bukkit.getWorld(worldName);
            if (targetWorld != null) {
                world = targetWorld;
            } else {
                player.sendMessage(ChatColor.RED + "World '" + worldName + "' not found. Using current world: " + world.getName());
            }
        }

        ItemStack brush = new ItemStack(Material.GOLDEN_HOE, 1);
        ItemMeta meta = brush.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Biome Brush");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Biome: " + ChatColor.AQUA + biome.name());
        lore.add(ChatColor.GRAY + "Radius: " + ChatColor.AQUA + radius);
        lore.add(ChatColor.GRAY + "World: " + ChatColor.AQUA + world.getName());
        lore.add(ChatColor.DARK_GRAY + "Right-click a block to paint biome.");
        meta.setLore(lore);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_BIOME, PersistentDataType.STRING, biome.getKey().toString());
        pdc.set(KEY_RADIUS, PersistentDataType.INTEGER, radius);
        pdc.set(KEY_WORLD, PersistentDataType.STRING, world.getName());
        brush.setItemMeta(meta);

        player.getInventory().addItem(brush);
        player.sendMessage(ChatColor.GREEN + "Given biome brush for " + biome.name() + " with radius " + radius + " in world " + world.getName() + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("give");
        }
        if (args.length == 2) {
            String prefix = args[1].toUpperCase(Locale.ROOT);
            List<String> list = new ArrayList<>();
            for (Biome b : Biome.values()) {
                if (b.name().startsWith(prefix)) list.add(b.name());
            }
            return list;
        }
        if (args.length == 3) {
            return Arrays.asList("3", "5", "8", "12", "16", "24", "32");
        }
        return Collections.emptyList();
    }
}
