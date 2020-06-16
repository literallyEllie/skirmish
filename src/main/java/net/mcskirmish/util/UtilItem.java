package net.mcskirmish.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class UtilItem {

    /**
     * Adds a glow to an item stack.
     * <p>
     * Gives the item the enchantment of {@link Enchantment#DURABILITY} level 1
     * and adds the {@link ItemFlag#HIDE_ATTRIBUTES} so it just looks magical.
     *
     * @param itemStack the itemstack to make glow
     * @return the glowing item
     */
    public static ItemStack addGlow(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.DURABILITY, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Creates an player head with the skin of a Minecraft textures hash
     *
     * @param textures the textures to use
     * @return the created item
     */
    public static ItemStack getSkull(String textures) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), (String) null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", textures).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        UtilReflect.setField(skullMeta, "profile", profile);

        skull.setItemMeta(skullMeta);
        return skull;
    }

}
