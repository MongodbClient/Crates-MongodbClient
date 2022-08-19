package de.mongodbclient.crates.config.objects;

import com.google.gson.JsonObject;
import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.builder.ItemBuilder;
import de.mongodbclient.crates.config.Database;
import org.bukkit.Material;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemKeyConfig {
    public ItemKeyConfig(String id) {
        String PATH = "plugins//Crates//database//MongodbClient-" + id + ".json";
        HashMap<String, Object> map = new HashMap<>();
        HashMap<Integer, Database> mapp = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            Map<String, Integer> s = new HashMap<>();
            s.put("KNOCKBACK", 1);
            if (i == 0) {
                mapp.put(i, new Database(new ItemBuilder(Material.IRON_SWORD)
                        .setDisplayName("Default Item")
                        .setLore("Default Lore")
                        .setAmount(1)
                        .setSubID((byte) 0)
                        .build(), 1, 10, s)
                );
            } else {
                mapp.put(i, new Database(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("Default Item")
                        .setLore("Default Lore")
                        .setAmount(1)
                        .setSubID((byte) 0)
                        .build(), 1, 10, new HashMap<>())
                );
            }
        }
        map.put(String.valueOf(id), mapp);
        Crates.getInstance().getConfigManager().registerConfig(new File(PATH), map);
    }

    public static HashMap<String, JsonObject> loadItemStack(String id) {
        String PATH = "plugins//Crates//database//MongodbClient-" + id + ".json";
        HashMap<String, JsonObject> list = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            list.put(String.valueOf(i), Crates.getInstance().getConfigManager().getConfig(new File(PATH)).get(id).getAsJsonObject().get(String.valueOf(i)).getAsJsonObject());
        }
        return list;
    }
}
