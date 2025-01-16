package eu.artbytefilip.prisonOriginals;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class PrisonOriginals extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());

        IEssentials essentials = (IEssentials) getServer().getPluginManager().getPlugin("Essentials");
        Bukkit.getPluginManager().registerEvents(new Core(this), this);

        Objects.requireNonNull(getCommand("sellall")).setExecutor(new PrisonMarket(essentials));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
