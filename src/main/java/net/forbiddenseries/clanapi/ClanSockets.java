package net.forbiddenseries.clanapi;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class ClanSockets {

    private static final ClanManager manager = SimpleClans.getInstance().getClanManager();
    private static final Gson gson = new Gson();

    public static void onMessage(String msg, JsonObject data) {
        String id = data.get("id").getAsString();
        switch (msg) {
            case "getclans":
                try {
                    List<Clan> clans = manager.getClans();
                    JsonObject json = new JsonObject();
                    json.add("clans", gson.toJsonTree(clans));
                    send("getclans_"+id, json, 200);
                } catch (Exception e) {
                    e.printStackTrace();                    
                }
                break;
            case "heartbeat":
                send("heartbeat_"+id, new JsonObject(), 200);                
            default:
                break;
        }
    }

    public static void send(String msg, JsonObject data, int code) {
        JsonObject json = new JsonObject();
        json.addProperty("message", "clanapip_"+msg);
        json.addProperty("code", code);
        json.add("data", data);
        SocketClient.getSocketClient().send(json.toString());
    }
}
