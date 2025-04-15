package net.forbiddenseries.clanapi;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;

public class ClanSockets {

    private static final ClanManager manager = new ClanManager();
    private static final Gson gson = new Gson();

    public static void onMessage(String msg, JsonObject data) {
        if (msg.equals("getclans")) {
            String id = data.get("id").getAsString();
            List<Clan> clans = manager.getClans();
            send("getclans_"+id, gson.toJsonTree(clans).getAsJsonObject());
        }
    }

    public static void send(String msg, JsonObject data) {
        JsonObject json = new JsonObject();
        json.addProperty("message", "clanapip_"+msg);
        json.add("data", data);
        SocketClient.getSocketClient().send(json.toString());
    }
}
