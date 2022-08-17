package de.mongodbclient.crates.key;

import de.mongodbclient.crates.Crates;
import de.mongodbclient.crates.config.objects.ItemKeyConfig;
import de.mongodbclient.crates.key.object.KeyObject;
import net.pretronic.databasequery.api.query.result.QueryResultEntry;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class KeyManager {

    public boolean isKeyExists(String key) {
        return !Crates.getInstance().getMySQL().getDatabaseCollection().find().where("key", key).execute().isEmpty();
    }


    public boolean isKeyExists(int id) {
        return !Crates.getInstance().getMySQL().getDatabaseCollection().find().where("id", String.valueOf(id)).execute().isEmpty();
    }

    public void deleteKey(String key) {
        Crates.getInstance().getMySQL().getDatabaseCollection().delete()
                .where("key", key)
                .executeAsync();
    }

    public KeyObject asObject(String key) {
        QueryResultEntry resultEntry = Crates.getInstance().getMySQL().getDatabaseCollection().find().where("key", key).execute().first();
        return new KeyObject(resultEntry.getString("id"), resultEntry.getString("creator"), resultEntry.getString("key"), resultEntry.getString("owner"), getActiveKeys(resultEntry.getString("key")));
    }

    public KeyObject asObject(int id) {
        QueryResultEntry resultEntry = Crates.getInstance().getMySQL().getDatabaseCollection().find().where("id", String.valueOf(id)).execute().first();
        return new KeyObject(resultEntry.getString("id"), resultEntry.getString("creator"), resultEntry.getString("key"), resultEntry.getString("owner"), getActiveKeys(resultEntry.getString("key")));
    }

    public List<KeyObject> getKeys() {
        List<KeyObject> arrayList = new ArrayList<>();
        for (QueryResultEntry resultEntry : Crates.getInstance().getMySQL().getDatabaseCollection().find().execute().asList()) {
            arrayList.add(new KeyObject(resultEntry.getString("id"), resultEntry.getString("creator"), resultEntry.getString("key"), resultEntry.getString("owner"), getActiveKeys(resultEntry.getString("key"))));
        }
        return arrayList;
    }

    public void updateSkullName(String key, String name) {
        Crates.getInstance().getMySQL().getDatabaseCollection().update().set("owner", name).where("key", key).execute();
    }

    public void addKey(CommandSender commandSender, String key) {
        Crates.getInstance().getMySQL().getDatabaseCollection().insert()
                .set("creator", commandSender.getName())
                .set("key", key)
                .set("owner", "MongodbClient")
                .execute();
        new ItemKeyConfig(key);
    }

    public int getActiveKeys(String key) {
        return Crates.getInstance().getMySQL().getDatabaseUserCollection().find().where("crate", key).execute().asList().size();
    }
}
