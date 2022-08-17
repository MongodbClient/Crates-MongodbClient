package de.mongodbclient.crates.listener;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.gui.CrateOpener;
import de.mongodbclient.crates.user.UserManager;
import de.mongodbclient.crates.utils.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventListener implements Listener {

    @EventHandler
    public void onPLayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        if (event.getItem().getType() == Material.PLAYER_HEAD) {
            if (!event.getItem().getItemMeta().getLore().isEmpty()) {
                String lore = event.getItem().getItemMeta().getLore().get(0);
                if (lore.startsWith("§e")) {
                    if (PlayerUtils.animationWait.get(event.getPlayer().getName()) != null) return;
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        UserManager userManager = UserManager.formUser(event.getPlayer());
                        boolean hasKey = userManager.hasKey(Integer.parseInt(lore.split("§e")[1]));
                        if (!hasKey) {
                            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Crates.getInstance().getMessageObject().getContent().get("actionbar-none-keys").getAsString()));
                            return;
                        }
                        int i = Integer.parseInt(lore.split("§e")[1]);
                        userManager.removeKey(i);
                        event.setCancelled(true);
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                        new CrateOpener(event.getPlayer()).openCrate(Crates.getInstance().getKeyManager().asObject(i).getKey());
                    }
                }
            }
        }
    }
}
