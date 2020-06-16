package net.mcskirmish.network;

public enum ServerGroup {

    LOBBY("Lobby-\\d+"),
    GAME(""); // todo, prison or something

    private final String pattern;

    /**
     * Represents a server group which all servers should fall into.
     * <p>
     * For personal development servers, this should not apply.
     *
     * @param pattern the regex pattern for a server to be in a group
     */
    ServerGroup(String pattern) {
        this.pattern = pattern;
    }

    public static ServerGroup fromName(String serverId) {
        for (ServerGroup group : values()) {
            if (group.isInGroup(serverId))
                return group;
        }

        return null;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isLobby() {
        return this == LOBBY;
    }

    /**
     * Matches the provided server id with the provided pattern of <code>this</code>
     *
     * @param serverId server id to check
     * @return if the server id matches the pattern of the group
     */
    public boolean isInGroup(String serverId) {
        return serverId.matches(pattern);
    }

}
