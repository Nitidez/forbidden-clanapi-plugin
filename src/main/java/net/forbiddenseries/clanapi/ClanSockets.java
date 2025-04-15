package net.forbiddenseries.clanapi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class ClanSockets {

    private static final ClanManager manager = SimpleClans.getInstance().getClanManager();
    private static final Gson gson = new Gson();

    public static void onMessage(String msg, JsonObject data) {
        String id = data.get("id").getAsString();
        switch (msg) {
            case "getclans": {
                List<Clan> clans = manager.getClans();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.add("clans", gson.toJsonTree(clans));
                send("getclans", json, 200);
                break;
            }
            case "getclan": {
                String tag = data.get("tag").getAsString();
                Clan clan = manager.getClan(tag);
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.add("clan", gson.toJsonTree(clan));
                send("getclan", json, 200);
                break;
            }
            case "clanonline": {
                String tag = data.get("tag").getAsString();
                Clan clan = manager.getClan(tag);
                List<ClanPlayer> online = clan != null ? clan.getOnlineMembers() : new ArrayList<>();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.add("online", gson.toJsonTree(online));
                send("clanonline", json, 200);
                break;
            }
            case "getplayers": {
                List<ClanPlayer> players = manager.getAllClanPlayers();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.add("players", gson.toJsonTree(players));
                send("getplayers", json, 200);
                break;
            }
            case "getplayer": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                ClanPlayer player = manager.getCreateClanPlayer(uuid);
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.add("player", gson.toJsonTree(player));
                send("getplayer", json, 200);
            }
            case "createclan": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                Player player = Bukkit.getPlayer(uuid);
                String colortag = data.get("colortag").getAsString();
                String clan_name = data.get("name").getAsString();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    manager.createClan(player, colortag, clan_name);
                    send("createclan", new JsonObject(), 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("createclan", json, 500);
                }
            }
            case "removeclan": {
                String tag = data.get("tag").getAsString();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    manager.removeClan(tag);
                    send("removeclan", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("removeclan", json, 500);
                }
            }
            case "addclanbb": {
                String tag = data.get("tag").getAsString();
                String announcer = data.has("announcer") ? data.get("announcer").getAsString() : "";
                String bb = data.get("bb").getAsString();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    if (data.has("announcer")) {
                        clan.addBb(announcer, bb);
                    } else {
                        clan.addBb(bb);
                    }
                    send("addclanbb", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("addclanbb", json, 500);
                }
            }
            case "clanrival": {
                String tag = data.get("tag").getAsString();
                String othertag = data.get("rival_tag").getAsString();
                boolean remove = data.get("remove").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    Clan otherclan = manager.getClan(othertag);
                    if (remove) {
                        clan.removeRival(otherclan);
                    } else {
                        clan.addRival(otherclan);
                    }
                    send("clanrival", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("clanrival", json, 500);
                }
            }
            case "clanally": {
                String tag = data.get("tag").getAsString();
                String othertag = data.get("ally_tag").getAsString();
                boolean remove = data.get("remove").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    Clan otherclan = manager.getClan(othertag);
                    if (remove) {
                        clan.removeAlly(otherclan);
                    } else {
                        clan.addAlly(otherclan);
                    }
                    send("clanally", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("clanally", json, 500);
                }
            }
            case "clanwar": {
                String tag = data.get("tag").getAsString();
                String othertag = data.get("war_tag").getAsString();
                boolean remove = data.get("remove").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    Clan otherclan = manager.getClan(othertag);
                    if (remove) {
                        clan.removeWarringClan(otherclan);
                    } else {
                        clan.addWarringClan(otherclan);
                    }
                    send("clanwar", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("clanwar", json, 500);
                }
            }
            case "clanmember": {
                String tag = data.get("tag").getAsString();
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                boolean remove = data.get("remove").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    ClanPlayer player = manager.getCreateClanPlayer(uuid);
                    if (remove) {
                        clan.removeMember(uuid);
                    } else {
                        clan.addPlayerToClan(player);
                    }
                    send("clanmember", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("clanmember", json, 500);
                }
            }
            case "clanpromote": {
                String tag = data.get("tag").getAsString();
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                boolean demote = data.get("demote").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    Clan clan = manager.getClan(tag);
                    if (demote) {
                        clan.demote(uuid);
                    } else {
                        clan.promote(uuid);
                    }
                    send("clanpromote", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("clanpromote", json, 500);
                }
            }
            case "playerrank": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                String rank = data.get("rank").getAsString();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    ClanPlayer player = manager.getCreateClanPlayer(uuid);
                    player.setRank(rank);
                    send("playerrank", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("playerrank", json, 500);
                }
            }
            case "playerleader": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                boolean setl = data.get("setleader").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    ClanPlayer player = manager.getCreateClanPlayer(uuid);
                    player.setLeader(setl);
                    send("playerleader", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("playerleader", json, 500);
                }
            }
            case "playertrusted": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                boolean sett = data.get("settrusted").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    ClanPlayer player = manager.getCreateClanPlayer(uuid);
                    player.setTrusted(sett);;
                    send("playertrusted", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("playertrusted", json, 500);
                }
            }
            case "playerpastclan": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                String tag = data.get("tag").getAsString();
                boolean remove = data.get("remove").getAsBoolean();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    ClanPlayer player = manager.getCreateClanPlayer(uuid);
                    if (remove) {
                        player.removePastClan(tag);
                    } else {
                        player.addPastClan(tag);
                    }
                    send("playerpastclan", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("playerpastclan", json, 500);
                }
            }
            case "deleteplayer": {
                UUID uuid = UUID.fromString(data.get("uuid").getAsString());
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                try {
                    manager.deleteClanPlayer(manager.getClanPlayer(uuid));
                    send("deleteplayer", json, 200);
                } catch (Exception e) {
                    json.addProperty("error", e.getMessage());
                    send("deleteplayer", json, 500);
                }
            }
            case "heartbeat": {
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                send("heartbeat", json, 200); 
            }               
            default: {
                break;
            }
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
