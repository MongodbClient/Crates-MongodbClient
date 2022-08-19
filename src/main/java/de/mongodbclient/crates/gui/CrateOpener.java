package de.mongodbclient.crates.gui;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.builder.ItemBuilder;
import de.mongodbclient.crates.config.Database;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.config.objects.ItemKeyConfig;
import de.mongodbclient.crates.utils.PlayerUtils;
import de.mongodbclient.crates.webhook.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrateOpener {

    Player player;
    boolean start = true;
    List<ItemStack> items = new ArrayList<>();
    int time = 20 * 10;
    boolean stop = false;

    public CrateOpener(Player player) {
        this.player = player;
    }

    public void openCrate(String key) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, Crates.getInstance().getConfigObject().getOpenerItem().getInventoryName());
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7").build());
        }
        inventory.setItem(4 + 18, new ItemBuilder(Material.EMERALD).setDisplayName(Crates.getInstance().getConfigObject().getOpenerItem().getWinItem()).build());
        inventory.setItem(4, new ItemBuilder(Material.EMERALD).setDisplayName(Crates.getInstance().getConfigObject().getOpenerItem().getWinItem()).build());
        ItemKeyConfig.loadItemStack(key).entrySet().forEach(all -> {
            String json = Crates.getInstance().getConfigManager().getGson().toJson(all.getValue());
            Database database = Crates.getInstance().getConfigManager().getGson().fromJson(json, Database.class);
            ItemStack i = database.getItemStack();
            for (int a = 0; a < database.getCount(); a++) {
                if (database.getEnchantments().isEmpty()) {
                    items.add(new ItemBuilder(i)
                            .setAmount(database.getAmount())
                            .build());
                } else {
                    ItemStack i3 = i;
                    database.getEnchantments().entrySet().forEach(test -> {
                        i3.addEnchantment(Enchantment.getByName(test.getKey()), test.getValue());
                    });
                    i3.setAmount(database.getAmount());
                    items.add(i3);
                }
            }
        });
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Crates.getInstance(), () -> {
            if (!stop) {
                switchItems(inventory);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 20, 20);
            }
        }, 0, 5);
        player.openInventory(inventory);
        PlayerUtils.animationWait.put(player.getName(), inventory);
        Bukkit.getScheduler().runTaskLater(Crates.getInstance(), () -> {
            player.sendMessage(Crates.getInstance().getMessageObject().getContent().get("crate-win").getAsString().replace("{0}", inventory.getItem(13).getItemMeta().getDisplayName()));
            player.getInventory().addItem(inventory.getItem(13));
            PlayerUtils.animationWait.remove(player.getName());
            stop = true;
            player.closeInventory();
            ConfigObject.WebhookConfig webhookConfig = Crates.getInstance().getConfigObject().getWebhook(ConfigObject.Type.PLAYER);
            DiscordWebhook discordWebhook = new DiscordWebhook(webhookConfig.getWebhook());
            discordWebhook.setUsername(webhookConfig.getName());
            discordWebhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setAuthor(webhookConfig.getAuthor(), null, null)
                    .setDescription(webhookConfig.getMessage().replace("{0}", player.getName()).replace("{1}", key).replace("{2}", inventory.getItem(13).getItemMeta().getDisplayName()))
                    .setTitle(webhookConfig.getTitle())
                    .setColor(Color.GREEN)
                    .setFooter(webhookConfig.getFooter(), null)
            );
            try {
                discordWebhook.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, time);
    }

    public void switchItems(Inventory inventory) {
        if (start == true) {
            start = false;
            inventory.setItem(9, getNewItem());
            inventory.setItem(10, getNewItem());
            inventory.setItem(11, getNewItem());
            inventory.setItem(12, getNewItem());
            inventory.setItem(13, getNewItem());
            inventory.setItem(14, getNewItem());
            inventory.setItem(15, getNewItem());
            inventory.setItem(16, getNewItem());
            inventory.setItem(17, getNewItem());
        } else {
            inventory.setItem(9, inventory.getItem(10));
            inventory.setItem(10, inventory.getItem(11));
            inventory.setItem(11, inventory.getItem(12));
            inventory.setItem(12, inventory.getItem(13));
            inventory.setItem(13, inventory.getItem(14));
            inventory.setItem(14, inventory.getItem(15));
            inventory.setItem(15, inventory.getItem(16));
            inventory.setItem(16, inventory.getItem(17));
            inventory.setItem(17, getNewItem());
        }
    }

    public ItemStack getNewItem() {
        return items.get(new Random().nextInt(items.size()));
    }
}
