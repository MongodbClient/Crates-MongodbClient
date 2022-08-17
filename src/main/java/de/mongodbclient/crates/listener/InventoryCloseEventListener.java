package de.mongodbclient.crates.listener;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;


public class InventoryCloseEventListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (PlayerUtils.animationWait.get(event.getPlayer().getName()) != null) {
            Bukkit.getScheduler().runTaskLater(Crates.getInstance(), () -> {
                Inventory inventory = PlayerUtils.animationWait.get(event.getPlayer().getName());
                if (PlayerUtils.animationWait.get(event.getPlayer().getName()) != null) {
                    event.getPlayer().openInventory(PlayerUtils.animationWait.get(event.getPlayer().getName()));
                }
            }, 5);
        }
    }
}
