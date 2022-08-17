package de.mongodbclient.crates.config;

import com.google.gson.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class MessageConfig {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    public void registerConfig(Map<String, Object> contentMap, File file) {
        Path path = Paths.get(file.getPath());
        if (Files.notExists(path)) {
            Files.createDirectories(path.getParent());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            outputStreamWriter.write(gson.toJson(contentMap));
            outputStreamWriter.close();
        }
    }

    @SneakyThrows
    public JsonObject getConfig(File file) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(file.getPath())), StandardCharsets.UTF_8)) {
            JsonElement jsonElement = new JsonParser().parse(inputStreamReader);
            if (jsonElement.isJsonObject()) {
                return jsonElement.getAsJsonObject();
            }
        }
        return new JsonObject();
    }


}
