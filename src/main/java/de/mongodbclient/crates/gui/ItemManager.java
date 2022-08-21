package de.mongodbclient.crates.gui;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.builder.ItemBuilder;
import de.mongodbclient.crates.config.Database;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.config.objects.ItemKeyConfig;
import de.mongodbclient.crates.key.object.KeyObject;
import de.mongodbclient.crates.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

    KeyObject keyObject;
    Player player;

    public ItemManager(KeyObject keyObject, Player player) {
        this.keyObject = keyObject;
        this.player = player;
    }

    public void openKey() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, keyObject.getKey());
        ConfigObject.InventoryItems inventoryItems = Crates.getInstance().getConfigObject().getInventoryItems();
        String[] lore = inventoryItems.getItemLore();
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : lore) {
            stringBuilder.append(s + "§8§l§7§2§5");
        }
        String replacer = stringBuilder.toString().replace("{0}", keyObject.getCreator()).replace("{1}", keyObject.getKey()).replace("{2}", String.valueOf(keyObject.getActiveLicenses()));
        String[] list = replacer.split("§8§l§7§2§5");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7").build());
        }
        ItemStack itemStack = new ItemBuilder(Material.PAPER).setDisplayName(inventoryItems.getItemDisplay().replace("{0}", keyObject.getKey()).replace("%id%", keyObject.getId())).addLoreArray(list).build();
        inventory.setItem(13, itemStack);
        ItemStack its2 = Crates.getInstance().getSkullBuilder().getSkull(keyObject.getPlayerSkull());
        inventory.setItem(0, its2);
        ItemKeyConfig.loadItemStack(keyObject.getKey()).entrySet().forEach(all -> {
            String json = Crates.getInstance().getConfigManager().getGson().toJson(all.getValue());
            Database database = Crates.getInstance().getConfigManager().getGson().fromJson(json, Database.class);
            ItemStack i = database.getItemStack();
            inventory.setItem(Integer.valueOf(all.getKey()) + 27, new ItemBuilder(i)
                            .setAmount(database.getAmount())
                    .setLore(inventoryItems.getAmount().replace("{3}", String.valueOf(database.getCount()))).build());
        });
        player.openInventory(inventory);
        player.updateInventory();
        PlayerUtils.playerInventory.put(player.getName(), keyObject.getKey());
    }
}
