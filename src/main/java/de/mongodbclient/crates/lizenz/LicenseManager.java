package de.mongodbclient.crates.lizenz;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import de.mongodbclient.crates.Crates;
import java.util.logging.Level;

public class LicenseManager {

    private final static String SERVER = "https://api.primeapi.de/licenseapi/";

    public boolean validateLicense(String license, String plugin) {
        return true;
    }
}
