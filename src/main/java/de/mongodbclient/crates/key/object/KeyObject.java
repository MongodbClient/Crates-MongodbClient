package de.mongodbclient.crates.key.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyObject {

    final String id;
    final String creator;
    final String key;
    final String playerSkull;
    final int activeLicenses;
}
