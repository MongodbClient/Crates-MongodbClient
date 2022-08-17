package de.mongodbclient.crates.user;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.key.object.KeyObject;
import net.pretronic.databasequery.api.query.result.QueryResultEntry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserManager {

    Player player;

    public UserManager(Player player) {
        this.player = player;
    }

    public static UserManager formUser(Player player) {
        return new UserManager(player);
    }

    public UserManager addCrateSkulls(int amount, Player player, KeyObject keyObject) {
        SkullMeta skullMeta = (SkullMeta) Crates.getInstance().getSkullBuilder().getSkull(keyObject.getPlayerSkull()).getItemMeta();
        skullMeta.setDisplayName(Crates.getInstance().getConfigObject().getCrateItem().getDisplay().replace("{0}", keyObject.getKey()));
        String[] list = {
                Crates.getInstance().getConfigObject().getSecurityKey() + "§e" + keyObject.getId(),
        };
        skullMeta.setLore(Arrays.asList(list));
        ItemStack itemStack = Crates.getInstance().getSkullBuilder().getSkull(keyObject.getPlayerSkull());
        itemStack.setItemMeta(skullMeta);
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
        return this;
    }

    public UserManager addKey(String crate, String add, String uuid) {
        Crates.getInstance().getMySQL().getDatabaseUserCollection().insert()
                .set("uuid", uuid)
                .set("crate", crate)
                .set("add", add)
                .execute();
        return this;
    }

    public void removeKey(int key) {
        List<Integer> arrayList = new ArrayList<>();
        for (QueryResultEntry resultEntry : Crates.getInstance().getMySQL().getDatabaseUserCollection().find().where("uuid", player.getUniqueId().toString()).where("crate", Crates.getInstance().getKeyManager().asObject(key).getKey()).execute().asList()) {
            arrayList.add(resultEntry.getInt("id"));
        }
        Crates.getInstance().getMySQL().getDatabaseUserCollection().delete().where("id", arrayList.get(0)).execute();
    }

    public boolean hasKey(int id) {
        if (Crates.getInstance().getKeyManager().isKeyExists(id)) {
            return !Crates.getInstance().getMySQL().getDatabaseUserCollection().find().where("uuid", player.getUniqueId().toString()).where("crate", Crates.getInstance().getKeyManager().asObject(id).getKey()).execute().isEmpty();
        }
        return false;
    }
}
