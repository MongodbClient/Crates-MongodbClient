package de.mongodbclient.crates.builder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class ItemBuilder {

    @Setter
    private ItemStack itemStack;
    @Getter
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setSubID(byte subID) {
        this.itemStack.getData().setData(subID);
        return this;
    }

    public ItemBuilder setLore(String lore) {
        this.itemMeta.setLore(Collections.singletonList(lore));
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        if (this.itemMeta.getLore() != null) {
            this.itemMeta.getLore().add(line);
        } else {
            List<String> lore = new ArrayList();
            lore.add(line);
            this.itemMeta.setLore(lore);
        }

        return this;
    }


    public ItemBuilder addLoreArray(String[] array) {
        if (this.itemMeta.getLore() != null) {
            String[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String current = var2[var4];
                this.itemMeta.getLore().add(current);
            }
        } else {
            List<String> lore = new ArrayList(Arrays.asList(array));
            this.itemMeta.setLore(lore);
        }

        return this;
    }

    public ItemBuilder setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setAmount(Integer amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(Map<Enchantment, Integer> map) {
        map.entrySet().forEach(all -> {
            this.itemMeta.addEnchant(all.getKey(), all.getValue(), false);
        });
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer power) {
        this.itemMeta.addEnchant(enchantment, power, false);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}