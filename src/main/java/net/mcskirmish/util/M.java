package net.mcskirmish.util;

import net.mcskirmish.rank.IRank;

public class M {

    public static final String NO_PERM = P.DENIED + C.IC + "You do not have permission.";
    public static final String NO_FOUND = C.IC + "Could not find " + C.V;

    /**
     * Generic messages of the server.
     * <p>
     * All of these messages have prefixed ({@link P}) and uses the network color scheme ({@link C})
     */
    M() {
    }

    public static String noPerm(IRank required) {
        return NO_PERM + " You need " + required.getPrefix() + C.IC + "+";
    }

}
