package net.mcskirmish.util;

import net.mcskirmish.account.Rank;

public class M {

    public static final String NO_PERM = P.DENIED + C.IC + "You do not have permission.";
    public static final String NO_FOUND = C.IC + "Could not find " + C.V;

    public static String noPerm(Rank required) {
        return NO_PERM + " You need " + required.getPrefix() + C.IC + "+";
    }

}
