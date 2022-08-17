package de.mongodbclient.crates.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Database {

    final ItemStack itemStack;
    final int count;
    final Map<String, Integer> enchantments;
}
