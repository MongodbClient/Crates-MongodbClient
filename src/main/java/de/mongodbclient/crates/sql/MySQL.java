package de.mongodbclient.crates.sql;

import com.zaxxer.hikari.HikariDataSource;
import de.mongodbclient.crates.config.objects.ConfigObject;
import lombok.Getter;
import net.pretronic.databasequery.api.Database;
import net.pretronic.databasequery.api.collection.DatabaseCollection;
import net.pretronic.databasequery.api.collection.field.FieldOption;
import net.pretronic.databasequery.api.datatype.DataType;
import net.pretronic.databasequery.api.driver.DatabaseDriver;
import net.pretronic.databasequery.api.driver.DatabaseDriverFactory;
import net.pretronic.databasequery.sql.dialect.Dialect;
import net.pretronic.databasequery.sql.driver.config.SQLDatabaseDriverConfigBuilder;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;

@Getter
public class MySQL {
    private Database database;
    private DatabaseCollection databaseCollection;
    private DatabaseCollection databaseUserCollection;

    public void createConnection(ConfigObject.SQLData sqlData) {
        Bukkit.getLogger();
        DatabaseDriver databaseDriver = DatabaseDriverFactory.create(
                sqlData.getDatabase(),
                new SQLDatabaseDriverConfigBuilder()
                        .setDialect(Dialect.MYSQL)
                        .setAddress(new InetSocketAddress(sqlData.getHost(), sqlData.getPort()))
                        .setDataSourceClassName(HikariDataSource.class.getName())
                        .setUsername(sqlData.getUser())
                        .setPassword(sqlData.getPassword())
                        .build());
        databaseDriver.connect();
        database = databaseDriver.getDatabase(sqlData.getDatabase());
    }

    public void createTable() {
        databaseCollection = database.createCollection("crate_keys")
                .field("id", DataType.INTEGER, FieldOption.PRIMARY_KEY, FieldOption.AUTO_INCREMENT)
                .field("creator", DataType.STRING)
                .field("key", DataType.STRING)
                .field("owner", DataType.STRING)
                .create();

        databaseUserCollection = database.createCollection("crate_users")
                .field("id", DataType.INTEGER, FieldOption.PRIMARY_KEY, FieldOption.AUTO_INCREMENT)
                .field("uuid", DataType.STRING)
                .field("crate", DataType.STRING)
                .field("add", DataType.STRING)
                .create();
    }

    public boolean isConnected() {
        return database.getDriver().isConnected();
    }

    public void close() {
        database.getDriver().disconnect();
    }
}
