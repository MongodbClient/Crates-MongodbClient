package de.mongodbclient.crates.config.objects;

import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class Database {

    final ItemStack itemStack;
    final int count;
}
