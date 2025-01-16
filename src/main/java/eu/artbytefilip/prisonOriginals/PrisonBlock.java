package eu.artbytefilip.prisonOriginals;

import org.bukkit.Material;

public class PrisonBlock {
    private final String name;
    private final Material material;
    private final int cooldown;
    private float price;

    public PrisonBlock(String name, Material material, int cooldown, float price) {
        this.name = name;
        this.material = material;
        this.cooldown = cooldown;
        this.price = price;
    }

    public String getName() {
        return name;
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
