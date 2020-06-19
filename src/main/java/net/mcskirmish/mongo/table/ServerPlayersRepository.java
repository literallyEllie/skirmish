package net.mcskirmish.mongo.table;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.mongo.MongoRepository;

public class ServerPlayersRepository extends MongoRepository {

    public ServerPlayersRepository(SkirmishPlugin plugin) {
        super((plugin.isDevServer() ? "DEV_" : "") + "SERVER_PLAYERS", plugin);
    }

}
