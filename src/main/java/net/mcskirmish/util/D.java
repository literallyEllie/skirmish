package net.mcskirmish.util;

public class D {

    /**
     * Print out a debug message to {@link System#out}
     * prefixed "[DEBUG]"
     *
     * @param object the object to prefix
     */
    public static void d(Object object) {
        System.out.println("[DEBUG] " + object);
    }

}
