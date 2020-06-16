package net.mcskirmish.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;

public class UtilServer {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static String PUBLIC_IP;

    public static int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    public static int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public static void broadcast(String string) {
        // TODO
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(string);
        Bukkit.broadcastMessage("");
    }

    public static String getPublicIp() {
        if (PUBLIC_IP == null) {
            try {
                URL getUrl = new URL("https://api.ipify.org");
                BufferedReader in = new BufferedReader(new InputStreamReader(getUrl.openStream()));
                return PUBLIC_IP = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return "0.0.0.0";
            }
        }

        return PUBLIC_IP;
    }

    public static double getTps() {
        return new BigDecimal(MinecraftServer.getServer().recentTps[0])
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

}
