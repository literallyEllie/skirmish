package net.mcskirmish.mongo.table;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.mongo.MongoRepository;

public class AccountsRepository extends MongoRepository {

    public AccountsRepository(SkirmishPlugin plugin) {
        super("ACCOUNTS", plugin);
    }

}
