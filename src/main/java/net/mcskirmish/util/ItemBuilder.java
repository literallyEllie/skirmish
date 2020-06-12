package net.mcskirmish.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private Material material;
    private int amount;
    private byte data;
    private short durability;

    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;

    private Color color;
    private PotionEffectType potionType;
    private List<PotionEffect> customEffects;
    private String skullOwner, skullTexture;
    private boolean glow, unbreaking;

    /**
     * Utility for creating items easily.
     *
     * @param material the material to use
     * @param amount the amount in the stack
     * @param data the special data
     */
    public ItemBuilder(Material material, int amount, byte data) {
        this.material = material;
        this.amount = amount;
        this.data = data;
    }

    /**
     * Utility for creating items easily.
     *
     * Sets the special data to 0.
     *
     * @param material the base material to use
     * @param amount the amount in the stack
     */
    public ItemBuilder(Material material, int amount) {
        this(material, amount, (byte) 0);
    }

    /**
     * Utility for creating items easily.
     *
     * Sets the amount to 1 and the special data of 0.
     *
     * @param material the base material to use
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Sets the item type of the item
     *
     * @param material new material
     * @return builder instance
     */
    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the amount of items in the stack
     *
     * @param amount the amount
     * @return builder instance
     */
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the special data of the item
     *
     * @param data special data byte
     * @return builder instance
     */
    public ItemBuilder setData(byte data) {
        this.data = data;
        return this;
    }

    /**
     * Sets the durability of the item
     *
     * @param durability durability to set
     * @return builder instance
     */
    public ItemBuilder setDurability(short durability) {
        this.durability = durability;
        return this;
    }

    /**
     * Sets the durability to a specified percentage.
     *
     * It is based off {@link Material#getMaxDurability()}
     *
     * @param durabilityPercent percentage to have the durabilty of
     * @return builder instance
     */
    public ItemBuilder setDurabilityPercent(double durabilityPercent) {
        return setDurability((short) (durabilityPercent * (double) material.getMaxDurability()));
    }

    // meta

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the lore of an item overwriting any existing lore
     *
     * @param lore lore list
     * @return builder instance
     */
    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Appends an array of lore lines to the lore
     *
     * The lines will be prefixed with {@link org.bukkit.ChatColor#GRAY}
     *
     * @param lines lines to add
     * @return builder instance
     */
    public ItemBuilder addLore(String... lines) {
        if (lore == null)
            lore = Lists.newArrayList();
        for (String line : lines) {
            lore.add(ChatColor.GRAY + line);
        }
        return this;
    }

    /**
     * Adds a blank lore line.
     *
     * @return builder instance
     */
    public ItemBuilder addLore() {
        addLore("");
        return this;
    }

    /**
     * Removes all the lore lines
     *
     * @return builder instance
     */
    public ItemBuilder removeLore() {
        if (lore != null)
            lore.clear();
        return this;
    }

    /**
     * Adds an enchant with the specified enchant.
     *
     * If the level is less than 1, it will remove the enchant if it is present.
     *
     * @param enchantment the enchant to add
     * @param level the level of the enchantment
     * @return builder instance
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        if (enchantment == null)
            return this;

        if (level > 0) {
            if (enchantments == null)
                enchantments = Maps.newHashMap();

            enchantments.put(enchantment, level);
        }
        else
            enchantments.remove(enchantment);

        return this;
    }

    /**
     * Removes the specified enchant
     *
     * @param enchantment the enchantment to remove
     * @return builder instance
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        if (enchantment != null && enchantments != null)
            enchantments.remove(enchantment);
        return this;
    }

    // special meta

    /**
     * Sets the color of a leader armor item.
     *
     * @param color the color for the leather armor
     * @return builder instance
     */
    public ItemBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the main potion effect type of a {@link PotionMeta}
     *
     * @param potionType main potion effect
     * @return builder instance
     */
    public ItemBuilder setType(PotionEffectType potionType) {
        this.potionType = potionType;
        return this;
    }

    /**
     * Add an additional potion effect to a {@link PotionMeta} item
     *
     * @param type the effect type
     * @param amplifier the amplifier of the effect
     * @param duration the duration of the
     * @return builder instance
     */
    public ItemBuilder addEffect(PotionEffectType type, int amplifier, int duration) {
        if (customEffects == null)
            customEffects = Lists.newArrayList();
        customEffects.add(new PotionEffect(type, amplifier, duration));
        return this;
    }

    /**
     * Removes an additional potion effect if it exists.
     *
     * @param type the potion effect type to remove
     * @return builder instance
     */
    public ItemBuilder removeEffect(PotionEffectType type) {
        if (customEffects != null) {
            customEffects.removeIf(potionEffect -> potionEffect.getType() == type);
        }
        return this;
    }

    /**
     * Removes all the additional effects to a potion
     *
     * @return builder instance
     */
    public ItemBuilder clearEffects() {
        if (customEffects != null)
            customEffects.clear();
        return this;
    }

    /**
     * Sets the skull owner of a {@link SkullMeta} item.
     *
     * Also sets the durability to 3 which is that of a player head.
     *
     * @param owner player name
     * @return builder instance
     */
    public ItemBuilder setSkullOwner(String owner) {
        this.skullOwner = owner;
        setDurability((byte) 3);
        return this;
    }

    /**
     * Sets the skull texture of the item.
     *
     * @see UtilItem#getSkull(String)
     *
     * @param skullTexture skull texture hash to use
     * @return builder instance
     */
    public ItemBuilder setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
        return this;
    }

    // attributes

    /**
     * Sets an item to be glowing or not.
     *
     * If the type is {@link Material#GOLDEN_APPLE} and glow is <code>true</code>, it will make it a God apple.
     *
     * @param glow whether the item should glow or not
     * @return builder instance
     */
    public ItemBuilder setGlow(boolean glow) {
        this.glow = glow;

        if (glow && material == Material.GOLDEN_APPLE) {
            data = 1;
            this.glow = false;
        }

        return this;
    }

    /**
     * Sets whether an item should be unbreaking or not.
     *
     * Uses spigot item tags.
     *
     * @param unbreaking if it should be unbreaking
     * @return builder instance
     */
    public ItemBuilder setUnbreaking(boolean unbreaking) {
        this.unbreaking = unbreaking;
        return this;
    }

    /**
     * Constructs the item using the provided modifiers
     *
     * @return the constructed item
     */
    public ItemStack toItem() {
        ItemStack itemStack = new ItemStack(material, amount, durability, data);
        if (skullTexture != null)
            itemStack = UtilItem.getSkull(skullTexture);

        ItemMeta itemMeta = itemStack.getItemMeta();

        // base meta
        if (name != null)
            itemMeta.setDisplayName(name);

        if (lore != null)
            itemMeta.setLore(lore);

        if (enchantments != null)
            itemStack.addEnchantments(enchantments);

        // unique meta
        if (color != null && itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        }
        if (potionType != null && itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setMainEffect(potionType);
        }
        if (customEffects != null && itemMeta instanceof PotionMeta) {
            customEffects.forEach(potionEffect -> ((PotionMeta) itemMeta).addCustomEffect(potionEffect, true));
        }
        if (skullOwner != null && itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(skullOwner);
        }

        // Fun stuff
        if (glow) {
            UtilItem.addGlow(itemStack);
        }

        if (unbreaking) {
            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
