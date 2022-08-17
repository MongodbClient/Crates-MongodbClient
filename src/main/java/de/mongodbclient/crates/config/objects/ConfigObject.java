package de.mongodbclient.crates.config.objects;

import com.google.gson.JsonObject;
import de.mongodbclient.crates.Crates;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigObject {

    private final String PATH = "plugins/Crates/settings.json";


    public void addContent() {
        Map<String, Object> messages = new HashMap<>();
        messages.put("CommandPerms", "crates.command");
        messages.put("SecurityKey", "§8§e§l§7§a§a§a§8§1§0§5§1§1§1§8§2§8§0§7§2§5§5");
        messages.put("OpenerGui", new OpenerItem("§aGewonnenes Item", "§8┨ §aCrates"));
        messages.put("Database", new SQLData(3306, "localhost", "crates", "root", ""));
        messages.put("CrateItem", new CrateItem("§8┨ §aCrate §7● §e{0}"));
        messages.put("AnvilSkullEdit", new AnvilSkullGui("§8┨ §cAbbrechen", new String[]{
                "§8┨ §aCrate §7● §cEs wurde nichts am Kopf geändert!",
                "§8┨ §aCrate §7● §aDu hast die Änderung erfolgreich gespeichert!",
        }));
        messages.put("UIItems", new InventoryItems("§8┨ §eCrates", "§8┨ §aCrate §7● §d{0} §8(§e%id%§8)",
                "§8┨ §7Chance §7● §e{3}/100",
                "§8┨ §7Crate §7●§a klicken zum ändern!"
                , new String[]{
                "§8┨ §7Ersteller §7● §e{0}",
                "§8┨ §7Key §7● §e{1}",
                "§8┨ §7Aktive Keys §7● §e{2}",
        }));
        messages.put("AdminLog", new WebhookConfig(
                "testHook",
                "Der Administrator {0} hat das Crate `{1}` erstellt!",
                "MongoLogging",
                "CrateSystem - MongodbClient",
                "**Information**",
                "made with \uD83D\uDC97 by MongodbClient"
        ));
        messages.put("AddLog", new WebhookConfig(
                "testHook",
                "Der Administrator {0} hat dem Spieler {1} {2} Crate(s) '{3}' hinzugefügt",
                "MongoLogging",
                "CrateSystem - MongodbClient",
                "**Information**",
                "made with \uD83D\uDC97 by MongodbClient"
        ));
        messages.put("OpenLog", new WebhookConfig(
                "testHook",
                "Der Spieler {0} hat aus dem Crate `{1}` das Item '**{2}**' gewonnen!",
                "MongoLogging",
                "CrateSystem - MongodbClient",
                "**Information**",
                "made with \uD83D\uDC97 by MongodbClient"
        ));
        messages.put("DeleteLog", new WebhookConfig(
                "testHook",
                "Der Administrator {0} hat das Crate `{1}` gelöscht!",
                "MongoLogging",
                "CrateSystem - MongodbClient",
                "**Information**",
                "made with \uD83D\uDC97 by MongodbClient"
        ));
        Crates.getInstance().getConfigManager().registerConfig(new File(PATH), messages);
    }

    public JsonObject getContent() {
        return Crates.getInstance().getConfigManager().getConfig(new File(PATH));
    }

    public WebhookConfig getWebhook(Type type) {
        if (type == Type.ADD) {
            return Crates.getInstance().getConfigManager().readValue(getContent(), "AddLog", WebhookConfig.class);
        }
        if (type == Type.ADMIN) {
            return Crates.getInstance().getConfigManager().readValue(getContent(), "AdminLog", WebhookConfig.class);
        }
        if (type == Type.PLAYER) {
            return Crates.getInstance().getConfigManager().readValue(getContent(), "OpenLog", WebhookConfig.class);
        }
        if (type == Type.DELETE) {
            return Crates.getInstance().getConfigManager().readValue(getContent(), "DeleteLog", WebhookConfig.class);
        }
        return null;
    }

    public String getSecurityKey() {
        return getContent().get("SecurityKey").getAsString();
    }

    public SQLData getDatabaseData() {
        return Crates.getInstance().getConfigManager().readValue(getContent(), "Database", SQLData.class);
    }

    public AnvilSkullGui getAnvilSkull() {
        return Crates.getInstance().getConfigManager().readValue(getContent(), "AnvilSkullEdit", AnvilSkullGui.class);
    }

    public OpenerItem getOpenerItem() {
        return Crates.getInstance().getConfigManager().readValue(getContent(), "OpenerGui", OpenerItem.class);
    }

    public CrateItem getCrateItem() {
        return Crates.getInstance().getConfigManager().readValue(getContent(), "CrateItem", CrateItem.class);
    }

    public InventoryItems getInventoryItems() {
        return Crates.getInstance().getConfigManager().readValue(getContent(), "UIItems", InventoryItems.class);
    }

    public enum Type {
        ADMIN,
        ADD,
        PLAYER,
        DELETE
    }

    @Getter
    @AllArgsConstructor
    public class WebhookConfig {
        final String webhook;
        final String message;
        final String name;
        final String author;
        final String title;
        final String footer;
    }

    @Getter
    @AllArgsConstructor
    public class OpenerItem {
        final String winItem;
        final String inventoryName;
    }

    @Getter
    @AllArgsConstructor
    public class CrateItem {
        final String display;
    }

    @Getter
    @AllArgsConstructor
    public class AnvilSkullGui {
        final String cancel;
        final String[] messages;
    }

    @AllArgsConstructor
    @Getter
    public class InventoryItems {
        final String inventoryName;
        final String itemDisplay;
        final String amount;
        final String clicktochange;
        final String[] itemLore;
    }

    @Getter
    @AllArgsConstructor
    public class SQLData {
        final int port;
        final String host;
        final String database;
        final String user;
        final String password;
    }
}
