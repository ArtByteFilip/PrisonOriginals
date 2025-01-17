package eu.artbytefilip.prisonOriginals;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Core implements Listener {

    private final PrisonOriginals plugin;
    private final PrisonBlock[] minableBlocks = new PrisonBlock[]{
            new PrisonBlock("Prison Sandstone", Material.SANDSTONE, 100, 3),
            new PrisonBlock("Prison Cobblestone", Material.COBBLESTONE, 100, 5)
    };

    public Core(PrisonOriginals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        GameMode playerGamemode = player.getGameMode();
        Block block = e.getBlock();
        Material firstBlockType = block.getType();

        if (playerGamemode == GameMode.CREATIVE) {
            return;
        }

        e.setCancelled(true); // Zrušíme event

        for (PrisonBlock prisonBlock : minableBlocks) {
            if (block.getType() == prisonBlock.getMaterial()) {
                player.getInventory().addItem(createBlock(block, player, prisonBlock.getName(), prisonBlock.getPrice())); // Pridáme hráčovi blok
                block.setType(Material.BEDROCK); // Nastavíme blok na BEDROCK
                respawnBlock(prisonBlock.getCooldown(), block, firstBlockType); // Pošleme úlohu s oneskorením
                break; // Ak sa materiál zhoduje, zlomíme cyklus, aby sa ďalšie porovnania neuskutočnili
            }
        }
    }

    public void respawnBlock(int delayTicks, Block block, Material firstBlockType) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (block.getType() == Material.BEDROCK) {
                    block.setType(firstBlockType); // Vrátiť pôvodný blok
                }
            }
        }.runTaskLater(plugin, delayTicks);
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        GameMode playerGamemode = player.getGameMode();
        ItemStack itemInHandMain = player.getInventory().getItemInMainHand();
        ItemStack itemInHandOff = player.getInventory().getItemInOffHand();

        if (playerGamemode == GameMode.CREATIVE) {
            return;
        }

        ItemMeta metaMain = itemInHandMain.getItemMeta();
        ItemMeta metaOff = itemInHandOff.getItemMeta();

        for (PrisonBlock prisonBlock : minableBlocks) {
            if (hasMinedByLore(metaMain) && itemInHandMain.getType() == prisonBlock.getMaterial()) {
                e.setCancelled(true);
                return;
            }
            if (hasMinedByLore(metaOff) && itemInHandOff.getType() == prisonBlock.getMaterial()) {
                e.setCancelled(true);
            }
        }
    }

    private boolean hasMinedByLore(ItemMeta itemMeta) {
        if (itemMeta == null || !itemMeta.hasLore()) {
            return false;
        }

        for (String loreLine : Objects.requireNonNull(itemMeta.getLore())) {
            if (loreLine.contains("Mined by:")) {
                return true;
            }
        }
        return false;
    }

    private ItemStack createBlock(Block block, Player player, String name, float price) {
        ItemStack item = new ItemStack(block.getType());
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Mined by: " + ChatColor.WHITE + player.getName());
        lore.add(ChatColor.AQUA + "Price per once: " + ChatColor.WHITE + price + "€");
        meta.setLore(lore);

        meta.setCustomModelData(12);

        item.setItemMeta(meta);
        return item;
    }
}
