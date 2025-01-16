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
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
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

            int amount = item.getAmount();

            for (String loreLine : Objects.requireNonNull(meta.getLore())) {
                if (loreLine.contains("Mined by:")) {
                    try {
                        item.setAmount(0);
                        if (player.isOnline()) {
                            User user = essentials.getUser(player); // Získanie User objektu z EssentialsX
                            user.giveMoney(BigDecimal.valueOf(1.0));
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
}
