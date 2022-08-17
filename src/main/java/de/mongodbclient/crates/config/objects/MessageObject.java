package de.mongodbclient.crates.config.objects;

import com.google.gson.JsonObject;
import de.mongodbclient.crates.Crates;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageObject {

    private final String PATH = "plugins/Crates/messages/config.json";


    public void addContent() {
        Map<String, Object> messages = new HashMap<>();
        messages.put("noperms", "§8┨ §eCrates §7● §cDazu hast du keine Rechte!");
        messages.put("crate-win", "§8┨ §eCrates §7● §aDu hast §e{0}§a gewonnen!");
        messages.put("actionbar-none-keys", "§8┨ §eCrates §7● §cDu hast keine Crates mehr!");
        messages.put("add-successfully", "§8┨ §eCrates §7● §aDu hast §e{0} §8(§e{1}§8) §aCrate(s) hinzugefügt!");
        messages.put("add-not-online", "§8┨ §eCrates §7● §cDieser Spieler wurde nicht gefunden!");
        messages.put("add-key-notfound", "§8┨ §eCrates §7● §cDieser Key wurde nicht gefunden!");
        messages.put("add-not-integer", "§8┨ §eCrates §7● §cBitte gebe eine Zahl an!");
        messages.put("delete-not-exists", "§8┨ §eCrates §7● §c'{0}' wurde nicht gefunden!");
        messages.put("only-ingame", "§8┨ §eCrates §7● §cDiesen Command kannst du nur Ingame ausführen!");
        messages.put("delete-successfully", "§8┨ §eCrates §7● §eDu hast erfolgreich '{0}' gelöscht!!");
        messages.put("creat-already-exists", "§8┨ §eCrates §7● §c'{0}' wurde bereits gefunden!");
        messages.put("create-successfully", "§8┨ §eCrates §7● §aDu hast erfolgreich '{0}' erstellt!");
        messages.put("CommandHelpMap", new String[]{
                "§8┨ §eCrates §7● §7/crate create <Key>",
                "§8┨ §eCrates §7● §7/crate delete <Key>",
                "§8┨ §eCrates §7● §7/crate edit",
                "§8┨ §eCrates §7● §7/crate give <Key> <Count> <Player>",
        });
        Crates.getInstance().getConfigManager().registerConfig(new File(PATH), messages);
    }

    public JsonObject getContent() {
        return Crates.getInstance().getConfigManager().getConfig(new File(PATH));
    }
}
