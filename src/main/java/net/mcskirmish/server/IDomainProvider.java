package net.mcskirmish.server;

import net.mcskirmish.util.Domain;

public interface IDomainProvider {

    /**
     * Provides the domain value
     *
     * @param domain domain to get value of
     * @return the domain value
     */
    String get(Domain domain);

}
