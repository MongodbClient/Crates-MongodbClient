package de.mongodbclient.crates.command;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.gui.UiManager;
import de.mongodbclient.crates.user.UserManager;
import de.mongodbclient.crates.webhook.DiscordWebhook;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class CratesCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission(Crates.getInstance().getConfigObject().getContent().get("CommandPerms").getAsString())) {
            commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("noperms").getAsString());
            return true;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                String key = args[1];
                if (Crates.getInstance().getKeyManager().isKeyExists(key)) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("creat-already-exists").getAsString().replace("{0}", key));
                    return true;
                }
                Crates.getInstance().getKeyManager().addKey(commandSender, key);
                commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("create-successfully").getAsString().replace("{0}", key));
                ConfigObject.WebhookConfig webhookConfig = Crates.getInstance().getConfigObject().getWebhook(ConfigObject.Type.ADMIN);
                DiscordWebhook discordWebhook = new DiscordWebhook(webhookConfig.getWebhook());
                discordWebhook.setUsername(webhookConfig.getName());
                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setAuthor(webhookConfig.getAuthor(), null, null)
                        .setDescription(webhookConfig.getMessage().replace("{0}", commandSender.getName()).replace("{1}", key))
                        .setTitle(webhookConfig.getTitle())
                        .setColor(Color.RED)
                        .setFooter(webhookConfig.getFooter(), null)
                );
                discordWebhook.execute();
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                String key = args[1];
                if (!Crates.getInstance().getKeyManager().isKeyExists(key)) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("delete-not-exists").getAsString().replace("{0}", key));
                    return true;
                }
                Crates.getInstance().getKeyManager().deleteKey(key);
                commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("delete-successfully").getAsString().replace("{0}", key)
                );
                ConfigObject.WebhookConfig webhookConfig = Crates.getInstance().getConfigObject().getWebhook(ConfigObject.Type.DELETE);
                DiscordWebhook discordWebhook = new DiscordWebhook(webhookConfig.getWebhook());
                discordWebhook.setUsername(webhookConfig.getName());
                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setAuthor(webhookConfig.getAuthor(), null, null)
                        .setDescription(webhookConfig.getMessage().replace("{0}", commandSender.getName()).replace("{1}", key))
                        .setTitle(webhookConfig.getTitle())
                        .setColor(Color.RED)
                        .setFooter(webhookConfig.getFooter(), null)
                );
                discordWebhook.execute();
                return true;
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get(" only-ingame").getAsString());
                    return true;
                }
                new UiManager((Player) commandSender).openUi();
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("give")) {
                String key = args[1];
                String amount = args[2];
                String user = args[3];
                Player player = Bukkit.getPlayer(user);
                if (player == null) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("add-not-online").getAsString());
                    return true;
                }
                boolean exists = Crates.getInstance().getKeyManager().isKeyExists(key);
                if (!exists) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("add-key-notfound").getAsString());
                    return true;
                }
                boolean integer = isInteger(amount);
                if (!integer) {
                    commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("add-not-integer").getAsString());
                    return true;
                }
                UserManager userManager = UserManager.formUser(player);
                for (int i = 0; i < Integer.parseInt(amount); i++) {
                    userManager.addKey(key, commandSender.getName(), player.getUniqueId().toString());
                }
                userManager.addCrateSkulls(Integer.parseInt(amount), player, Crates.getInstance().getKeyManager().asObject(key));
                commandSender.sendMessage(Crates.getInstance().getMessageObject().getContent().get("add-successfully").getAsString()
                        .replace("{0}", player.getName())
                        .replace("{1}", amount)
                );
                ConfigObject.WebhookConfig webhookConfig = Crates.getInstance().getConfigObject().getWebhook(ConfigObject.Type.ADD);
                DiscordWebhook discordWebhook = new DiscordWebhook(webhookConfig.getWebhook());
                discordWebhook.setUsername(webhookConfig.getName());
                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setAuthor(webhookConfig.getAuthor(), null, null)
                        .setDescription(webhookConfig.getMessage().replace("{0}", commandSender.getName()).replace("{1}", player.getName()).replace("{2}", amount).replace("{3}", key))
                        .setTitle(webhookConfig.getTitle())
                        .setColor(Color.ORANGE)
                        .setFooter(webhookConfig.getFooter(), null)
                );
                discordWebhook.execute();
                return true;
            }
        }
        sendHelp(commandSender);
        return true;
    }

    public boolean isInteger(String s) {
        try {
            Integer i = Integer.parseInt(s);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void sendHelp(CommandSender commandSender) {
        String[] list = Crates.getInstance().getConfigManager().readValue(Crates.getInstance().getMessageObject().getContent(), "CommandHelpMap", String[].class);
        commandSender.sendMessage(list);
    }
}
