package de.mongodbclient.crates.builder;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.config.objects.ConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SkullBuilder {

    private final HashMap<String, ItemStack> stack = new HashMap<>();

    public ItemStack getSkull(String name) {
        if (stack.get(name + "playerhead") == null) {
            ConfigObject.InventoryItems inventoryItems = Crates.getInstance().getConfigObject().getInventoryItems();
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setDisplayName(inventoryItems.getClicktochange());
            Objects.requireNonNull(skullMeta).setOwningPlayer(Bukkit.getOfflinePlayer(name));
            itemStack.setItemMeta(skullMeta);
            stack.put(name + "playerhead", itemStack);
            return itemStack;
        } else {
            return stack.get(name + "playerhead");
        }
    }

    public ItemStack getSkull(String name, String display, int amount, List<String> lore) {
        if (stack.get(name + display) == null) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setDisplayName(display);
            skullMeta.setLore(lore);
            Objects.requireNonNull(skullMeta).setOwningPlayer(Bukkit.getOfflinePlayer(name));
            itemStack.setItemMeta(skullMeta);
            itemStack.setAmount(amount);
            stack.put(name + display, itemStack);
            return itemStack;
        } else {
            return stack.get(name + display);
        }
    }
}