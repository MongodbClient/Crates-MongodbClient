package de.mongodbclient.crates;

import de.mongodbclient.crates.builder.SkullBuilder;
import de.mongodbclient.crates.command.CratesCommand;
import de.mongodbclient.crates.config.ConfigManager;
import de.mongodbclient.crates.config.objects.ConfigObject;
import de.mongodbclient.crates.config.objects.MessageObject;
import de.mongodbclient.crates.key.KeyManager;
import de.mongodbclient.crates.listener.InventoryClickEventListener;
import de.mongodbclient.crates.listener.InventoryCloseEventListener;
import de.mongodbclient.crates.listener.PlayerInteractEventListener;
import de.mongodbclient.crates.lizenz.LicenseManager;
import de.mongodbclient.crates.sql.MySQL;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public class Crates extends JavaPlugin {

    @Getter
    private static Crates instance;
    private ConfigManager configManager;
    private MessageObject messageObject;
    private ConfigObject configObject;
    private KeyManager keyManager;
    private MySQL mySQL;
    private SkullBuilder skullBuilder;
    private LicenseManager licenseManager;

    @Override
    public void onEnable() {
        instance = this;
        licenseManager = new LicenseManager();
        if (!licenseManager.validateLicense("GIFT-FG3K-SBAD-DJLS", "PrivateBanSystem")) {
            this.getPluginLoader().disablePlugin(this);
            return;
        } else {
            configManager = new ConfigManager();
            messageObject = new MessageObject();
            configObject = new ConfigObject();
            messageObject.addContent();
            configObject.addContent();
            skullBuilder = new SkullBuilder();
            mySQL = new MySQL();
            mySQL.createConnection(configObject.getDatabaseData());
            if (mySQL.isConnected()) {
                mySQL.createTable();
            }
            keyManager = new KeyManager();

            this.getCommand("crates").setExecutor(new CratesCommand());
            this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
            this.getServer().getPluginManager().registerEvents(new InventoryCloseEventListener(), this);
        }
    }

    @Override
    public void onDisable() {
        if(mySQL != null) {
            if (mySQL.isConnected()) {
                mySQL.close();
            }
        }
    }
}
