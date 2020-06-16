package net.mcskirmish.mongo.table;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.mongo.MongoRepository;

public class ServerDataRepository extends MongoRepository {

    public ServerDataRepository(SkirmishPlugin plugin) {
        super((plugin.isDevServer() ? "DEV_" : "") + "SERVERS", plugin);
    }

}
