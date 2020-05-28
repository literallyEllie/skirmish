package net.mcskirmish.mongo.table;

import net.mcskirmish.mongo.MongoRepository;

public class AccountsRepository extends MongoRepository {

    public AccountsRepository() {
        super("ACCOUNTS");
    }

}
