package net.mcskirmish.mongo;

import com.google.gson.stream.JsonReader;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.util.UtilJson;
import org.bson.Document;
import org.bson.UuidRepresentation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MongoManager extends Module {

    public static final String DB_PRODUCTION = "skirm_prod",
            DB_DEVELOPMENT = "skirm_dev";
    private static final String PATH_MONGO = "config" + File.separator + "mongo.json";

    private MongoClient client;
    private MongoDatabase database;

    /**
     * Base wrapper used to access the database.
     * <p>
     * Access using a {@link MongoRepository} through {@link MongoManager#getCollection(MongoRepository)}
     *
     * @param plugin plugin instance
     */
    public MongoManager(SkirmishPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void start() {

        try (JsonReader jsonReader = new JsonReader(new FileReader(new File(PATH_MONGO)))) {
            final Map<String, String> values = UtilJson.fromConfig(jsonReader);

            MongoCredential credential = MongoCredential.createCredential(
                    values.get("username"),
                    plugin.isDevServer() ? DB_DEVELOPMENT : DB_PRODUCTION,
                    values.get("password").toCharArray()
            );

            client = new MongoClient(
                    new ServerAddress(values.get("host"), Integer.parseInt(values.get("port"))),
                    credential,
                    MongoClientOptions.builder()
                            .uuidRepresentation(UuidRepresentation.STANDARD)
                            .build()
            );

            database = client.getDatabase(plugin.isDevServer() ? DB_DEVELOPMENT : DB_PRODUCTION);
        } catch (IOException e) {
            plugin.error("failed to load mongo credentials", e);
        }

    }

    @Override
    protected void stop() {
        if (client != null)
            client.close();
    }

    /**
     * Gets the collection from a {@link MongoRepository} wrapper.
     * <p>
     * May be null if the is no database connected.
     *
     * @param table the table of the data
     * @return the {@link MongoCollection} of the table, or null if there is no connection.
     */
    public MongoCollection<Document> getCollection(MongoRepository table) {
        return database != null
                ? database.getCollection(table.getRepository())
                : null;
    }

}
