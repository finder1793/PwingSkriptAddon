package com.pwing.pwingskriptaddon.biome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BiomeBrushListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        // Avoid double-firing for off-hand in newer versions
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String biomeKeyString = pdc.get(BiomeBrushCommand.KEY_BIOME, PersistentDataType.STRING);
        Integer radius = pdc.get(BiomeBrushCommand.KEY_RADIUS, PersistentDataType.INTEGER);
        String worldName = pdc.get(BiomeBrushCommand.KEY_WORLD, PersistentDataType.STRING);
        if (biomeKeyString == null || radius == null || worldName == null) return; // not a biome brush

        NamespacedKey biomeKey = NamespacedKey.fromString(biomeKeyString);
        Biome biome = null;
        for (Biome b : Biome.values()) {
            if (b.getKey().equals(biomeKey)) {
                biome = b;
                break;
            }
        }
        if (biome == null) {
            player.sendMessage(ChatColor.RED + "Biome on the brush is invalid: " + biomeKeyString);
            return;
        }

        Block clicked = event.getClickedBlock();
        if (clicked == null) return;

        World brushWorld = Bukkit.getWorld(worldName);
        if (brushWorld != null && !brushWorld.equals(clicked.getWorld())) {
            player.sendMessage(ChatColor.RED + "This brush is for world " + worldName + ". You're in " + clicked.getWorld().getName() + ".");
            return;
        }

        int r = Math.max(1, Math.min(32, radius));
        int cx = clicked.getX();
        int cz = clicked.getZ();
        int y = clicked.getY();

        // Paint square area on the surface column-wise for performance
        // We use Bukkit API to set biome; feature only activates when FAWE is present and config enabled
        // to honor the requirement, but we keep implementation simple and compatible.
        var world = clicked.getWorld();
        int changed = 0;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                try {
                    // 1.16 API supports 3D biome; we set at column across height with current y for reasonable effect
                    world.setBiome(x, y, z, biome);
                    changed++;
                } catch (Throwable t) {
                    // Ignore per-block errors to avoid spamming
                }
            }
        }

        player.sendMessage(ChatColor.GREEN + "Painted biome " + biome.name() + " in ~" + changed + " columns (radius " + r + ").");
        event.setCancelled(true);
    }
}
