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
        try {
            String url = "https://api.primeapi.de/licenseapi/" + license;
            HttpResponse<String> response = Unirest.get(url).asString();
            if (response.getStatus() == 200) {
                JsonObject object = new JsonParser().parse(response.getBody()).getAsJsonObject();

                if (!(object.get("plugin") == null || object.get("plugin").isJsonNull() || object.get("plugin")
                        .getAsString()
                        .equalsIgnoreCase(
                                plugin))) {
                    Crates.getInstance()
                            .getLogger()
                            .log(Level.INFO, "Lizenz für " + plugin + " wurde abgelehnt: Invalid Plugin");
                    return false;
                }

                if (!(object.get("expiry").getAsLong() == -1 || object.get("expiry")
                        .getAsLong() >= System.currentTimeMillis())) {
                    Crates.getInstance()
                            .getLogger()
                            .log(Level.INFO, "Lizenz für Crates wurde abgelehnt: Lizenz expired");
                    return false;
                }

                if (object.get("block").getAsBoolean()) {
                    Crates.getInstance()
                            .getLogger()
                            .log(Level.INFO, "Lizenz für Crates wurde abgelehnt: Lizenz gesperrt");
                    return false;
                }

                Crates.getInstance().getLogger().log(Level.INFO, "Lizenz für Crates erfolgreich überprüft");
                return true;
            } else if (response.getStatus() == 404) {
                Crates.getInstance()
                        .getLogger()
                        .log(Level.INFO, "Lizenzüberprüfung fehlgeschlagen: Lizenz ist invalide!");
                return false;
            } else {
                Crates.getInstance()
                        .getLogger()
                        .log(Level.INFO, "Lizenzüberprüfung fehlgeschlagen: " + response.getBody());
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Crates.getInstance()
                    .getLogger()
                    .log(Level.INFO, "Lizenzabfrage für Crates fehlgeschlagen: " + ex.getMessage());
            return true;
        }
    }
}
