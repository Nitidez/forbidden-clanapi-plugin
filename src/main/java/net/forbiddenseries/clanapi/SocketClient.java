package net.forbiddenseries.clanapi;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class SocketClient extends WebSocketClient {

    private static SocketClient wsclient;
    private static final Main instance = Main.getInstance();
    private static String currentToken;
    private static URI serverUri;
    private static int reconnectAttempts = 0;

    private final int RECONNECT_DELAY_MS = 5000;

    private SocketClient(URI serverUri, Map<String, String> headers) {
        super(serverUri, headers);
    }

    public static void setupSocketClient(String url, String token) throws URISyntaxException {
        currentToken = token;
        serverUri = new URI(url);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        wsclient = new SocketClient(serverUri, headers);
        wsclient.connect();
        wsclient.getLogger().info("[WebSocket] Conexão inicial sendo feita...");
    }

    public static SocketClient getSocketClient() {
        return wsclient;
    }

    private Logger getLogger() {
        return instance.getLogger();
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        getLogger().info("[WebSocket] Conectado com sucesso.");
        JsonObject json = new JsonObject();
        json.addProperty("message", "clanapi_connected");
        send(json.toString());
        reconnectAttempts = 0;
    }

    @Override
    public void onMessage(String message) {
        try {
        JsonObject json = (new JsonParser()).parse(message).getAsJsonObject();
        String jsonmsg = json.get("message").getAsString();
        JsonObject jsondata = json.get("data").getAsJsonObject();
        if (jsonmsg.startsWith("clanapiw_")) {
            ClanSockets.onMessage(jsonmsg.replaceFirst("clanapiw_", ""), jsondata);
        }
        } catch (Exception e) {}
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        getLogger().warning("[WebSocket] Conexão encerrada. Motivo: " + reason);
        attemptReconnect();
    }

    @Override
    public void onError(Exception ex) {
        getLogger().severe("[WebSocket] Erro: " + ex.getMessage());
    }

    private void attemptReconnect() {
        if (reconnectAttempts < 3) {
            getLogger().info("[WebSocket] Tentando reconectar em " + (RECONNECT_DELAY_MS / 1000) + " segundos...");
        }

        reconnectAttempts++;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + currentToken);
                    wsclient = new SocketClient(serverUri, headers);
                    wsclient.connect();
                    if (reconnectAttempts <= 3) {
                        wsclient.getLogger().info("[WebSocket] Tentativa de reconexão...");
                    }
                } catch (Exception e) {
                    if (reconnectAttempts <= 3) {
                        wsclient.getLogger().severe("[WebSocket] Erro ao tentar reconectar: " + e.getMessage());
                    }
                }
            }
        }, RECONNECT_DELAY_MS);
    }
}
