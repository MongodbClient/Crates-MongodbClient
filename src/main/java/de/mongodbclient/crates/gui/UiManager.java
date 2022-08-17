package de.mongodbclient.crates.gui;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.builder.ItemBuilder;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.key.object.KeyObject;
import de.mongodbclient.crates.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class UiManager {

    Player player;
    HashMap<String, String> map = new HashMap<>();

    public UiManager(Player player) {
        this.player = player;
    }

    public void openUi() {
        ConfigObject.InventoryItems inventoryItems = Crates.getInstance().getConfigObject().getInventoryItems();
        List<KeyObject> keys = Crates.getInstance().getKeyManager().getKeys();
        int inventorySize = keys.size() / 9 + 1;
        Inventory inventory = Bukkit.createInventory(null, inventorySize * 9, inventoryItems.getInventoryName());
        HashMap<String, String> add = new HashMap<>();
        keys.forEach(all -> {
            String[] lore = inventoryItems.getItemLore();
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : lore) {
                stringBuilder.append(s + "§8§l§7§2§5");
            }
            String replacer = stringBuilder.toString()
                    .replace("{0}", all.getCreator())
                    .replace("{1}", all.getKey())
                    .replace("{2}", String.valueOf(all.getActiveLicenses()));
            String[] list = replacer.split("§8§l§7§2§5");
            ItemStack itemStack = new ItemBuilder(Material.PAPER)
                    .setDisplayName(inventoryItems.getItemDisplay().replace("{0}", all.getKey()).replace("%id%", all.getId()))
                    .addLoreArray(list)
                    .build();
            inventory.addItem(itemStack);
            add.put(inventoryItems.getItemDisplay().replace("{0}", all.getKey()).replace("%id%", all.getId()), all.getKey());
        });
        if (PlayerUtils.playerdatas.get(player) != null) {
            PlayerUtils.playerdatas.remove(player);
        }
        PlayerUtils.playerdatas.put(player.getName(), add);
        player.openInventory(inventory);
    }
}
