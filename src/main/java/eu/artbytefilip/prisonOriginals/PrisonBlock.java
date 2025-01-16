package eu.artbytefilip.prisonOriginals;

import org.bukkit.Material;

public class PrisonBlock {
    private final Material material;
    private final int cooldown;
    private float price;

    public PrisonBlock(Material material, int cooldown, float price) {
        this.material = material;
        this.cooldown = cooldown;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCooldown() {
        return cooldown;
    }

    public float getPrice() {
        return price;
    }
}
