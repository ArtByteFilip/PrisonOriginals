package eu.artbytefilip.prisonOriginals;

import org.bukkit.Material;

public class PrisonBlock {
    private final Material material;
    private final int cooldown;

    public PrisonBlock(Material material, int cooldown) {
        this.material = material;
        this.cooldown = cooldown;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCooldown() {
        return cooldown;
    }
}
