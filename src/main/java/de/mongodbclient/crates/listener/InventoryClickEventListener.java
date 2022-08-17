package de.mongodbclient.crates.listener;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.builder.ItemBuilder;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.gui.ItemManager;
import de.mongodbclient.crates.key.object.KeyObject;
import de.mongodbclient.crates.utils.PlayerUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ConfigObject.InventoryItems inventoryItems = Crates.getInstance().getConfigObject().getInventoryItems();
        if (event.getView().getTitle() != null) {
            if (event.getView().getTitle().equalsIgnoreCase(inventoryItems.getInventoryName())) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getType() != null) {
                        if (event.getCurrentItem().getType() != Material.AIR) {
                            if (event.getCurrentItem().getType() == Material.PAPER) {
                                if (PlayerUtils.playerdatas.get(event.getWhoClicked().getName()) != null) {
                                    String key = PlayerUtils.playerdatas.get(event.getWhoClicked().getName()).get(event.getCurrentItem().getItemMeta().getDisplayName());
                                    KeyObject keyObject = Crates.getInstance().getKeyManager().asObject(key);
                                    event.getWhoClicked().closeInventory();
                                    new ItemManager(keyObject, (Player) event.getWhoClicked()).openKey();
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
            if(event.getView().getTitle().equalsIgnoreCase(PlayerUtils.playerInventory.get(event.getWhoClicked().getName()))) {
                event.setCancelled(true);
            }
            if (event.getView().getTitle().equalsIgnoreCase(Crates.getInstance().getConfigObject().getOpenerItem().getInventoryName())) {
                event.setCancelled(true);
            }
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Crates.getInstance().getConfigObject().getInventoryItems().getClicktochange())) {
                ConfigObject.AnvilSkullGui anvilSkullGui = Crates.getInstance().getConfigObject().getAnvilSkull();
                AnvilGUI anvilGui = new AnvilGUI.Builder()
                        .onComplete((player, text) -> {
                            player.sendMessage(anvilSkullGui.getMessages()[1]);
                            Crates.getInstance().getKeyManager().updateSkullName(event.getView().getTitle(), text);
                            return AnvilGUI.Response.close();
                        })
                        .text(Crates.getInstance().getKeyManager().asObject(event.getView().getTitle()).getPlayerSkull())
                        .itemLeft(
                                new ItemBuilder(Material.IRON_SWORD)
                                        .setDisplayName(Crates.getInstance().getKeyManager().asObject(event.getView().getTitle()).getPlayerSkull())
                                        .build()
                        )
                        .itemRight(new ItemBuilder(Material.IRON_SWORD)
                                .setDisplayName(anvilSkullGui.getCancel())
                                .build())
                        .onRightInputClick(player -> {
                            player.sendMessage(anvilSkullGui.getMessages()[0]);
                            AnvilGUI.Response.close();
                            player.closeInventory();
                        })
                        .title(event.getView().getTitle())
                        .plugin(Crates.getInstance())
                        .open((Player) event.getWhoClicked());
            }
        }
    }
}
