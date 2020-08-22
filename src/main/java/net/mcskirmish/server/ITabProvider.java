package net.mcskirmish.server;

import net.mcskirmish.account.Account;

import java.util.List;

public interface ITabProvider {

    List<String> setHeader(Account account);

    List<String> setFooter(Account account);

}
