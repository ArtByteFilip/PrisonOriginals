package eu.artbytefilip.prisonOriginals;

import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Objects;

public class PrisonMarket implements Listener, CommandExecutor {

    private final IEssentials essentials;

    public PrisonMarket(IEssentials essentials) {
        this.essentials = essentials;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            SellAll(player);
        } else {
            commandSender.sendMessage("This command can by executed only by player.");
        }
        return true;
    }

    private void SellAll(Player player) {
        PlayerInventory inventory = player.getInventory();

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) {
                continue;
            }

            int amount = item.getAmount(); // Počet blokov v tomto ItemStacku
            double pricePerBlock = 0.0; // Cena za jeden blok
            boolean hasValidLore = false;

            for (String loreLine : Objects.requireNonNull(meta.getLore())) {
                if (loreLine.contains("Price per once:")) {
                    try {
                        String[] parts = loreLine.split(":");
                        pricePerBlock = Double.parseDouble(parts[1].replaceAll("[^0-9.]", "").trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing price from lore: " + e.getMessage());
                        pricePerBlock = 0.0; // Ak sa nepodarí načítať cenu, nastav na 0
                    }
                    hasValidLore = true;
                }
            }

            if (hasValidLore && pricePerBlock > 0) {
                try {
                    // Výpočet celkovej hodnoty a odstránenie blokov
                    double totalPrice = pricePerBlock * amount; // Celková hodnota ItemStacku
                    item.setAmount(0); // Odstránenie blokov

                    if (player.isOnline()) {
                        User user = essentials.getUser(player); // Získanie User objektu z EssentialsX
                        user.giveMoney(BigDecimal.valueOf(totalPrice)); // Pripočíta sumu podľa celkovej hodnoty
                    }
                } catch (NumberFormatException err) {
                    System.out.println("Error: " + err);
                } catch (MaxMoneyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
