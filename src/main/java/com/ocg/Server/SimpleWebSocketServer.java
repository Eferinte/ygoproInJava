package com.ocg.Server;

import com.google.gson.Gson;
import com.ocg.Client.ClientCard;
import com.ocg.Client.DuelClient;
import com.ocg.DuelMode;
import com.ocg.DuelPlayer;
import com.ocg.Game;
import com.ocg.SingleDuel;
import com.ocg.core.OCGDll;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.ocg.Constants.LOCATION_HAND;
import static com.ocg.Constants.LOCATION_MZONE;


public class SimpleWebSocketServer extends WebSocketServer {

    public static Game mainGame;
    public static SingleDuel duel;
    public static Gson JSON = new Gson();
    public static Long pduel;

    //用来存放每个客户端对应的MyWebSocket对象。
    private static Vector<DuelPlayer> duelists = new Vector<>();
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    public SimpleWebSocketServer(int port, Game game, SingleDuel duel_mode) throws UnknownHostException {
        super(new InetSocketAddress(port));
        mainGame = game;
        duel = duel_mode;
    }

    public String getId(WebSocket conn) {
        // TODO 抽象成接口
        return conn.getRemoteSocketAddress().getAddress().toString() + conn.getRemoteSocketAddress().getPort();
    }

    /**
     * 房间内其他人
     *
     * @param conn
     * @param msg
     */
    public void broadcastToOthersInRoom(WebSocket conn, String msg) {
        for (WebSocket ws : webSocketSet) {
            if (ws.equals(conn)) continue;
            ws.send(msg);
        }
    }

    /**
     * duel中其他人
     *
     * @param conn
     * @param msg
     */
    public void broadcastToOthersInGame(WebSocket conn, String msg) {
        for (DuelPlayer duelPlayer : duelists) {
            if (duelPlayer.conn.equals(conn)) continue;
            duelPlayer.conn.send(msg);
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send(MessageFormatter.format("Welcome to the server!", MessageFormatter.MessageType.MSG));
        String id = getId(conn);
        System.out.println(
                id + " entered the room!");
        conn.send(MessageFormatter.format(id, MessageFormatter.MessageType.AUTHORIZE));

        // 加载
        for (DuelPlayer duelist : duelists) {
            conn.send(MessageFormatter.format(duelist.getNetPacket(), MessageFormatter.MessageType.JOIN));
        }
        if (pduel != null) {
            for (DuelPlayer duelist : duelists) {
                if (duelist.id.equals(id)) {
                    //TODO reconnect
                    break;
                }
            }
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has left the room!");
        if (pduel == null) {
            System.out.println("begin remove");
            //若duel未开始，移除client
            for (DuelPlayer duelist : duelists) {
                if (duelist.id.equals(getId(conn))) {
                    duelists.remove(duelist);
                    System.out.println("remove " + duelist);
                    broadcast("msg-" + getId(conn) + "离开了桌位");
                    break;
                }
            }
        } else {
            //TODO waiting for reconnect
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            String res = MsgAnalyze(message, conn);
            for (DuelPlayer duelist : duelists) {
//                conn.send(MessageFormatter.format(duelist.getNetPacket(), MessageFormatter.MessageType.MSG));
            }
            if (res != null) {
                conn.send(MessageFormatter.format(res, MessageFormatter.MessageType.MSG));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific
            // websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public String MsgAnalyze(String msg, WebSocket conn) throws IOException {
        String res = null;
        MessageFormatter.Message formatMsg = JSON.fromJson(msg, MessageFormatter.Message.class);
        switch (formatMsg.type) {
            case MSG -> {
                broadcast(MessageFormatter.format(formatMsg.data, MessageFormatter.MessageType.MSG));
            }
            case JOIN -> {
                res = "尝试加入duel桌...\n";
                conn.send(MessageFormatter.format(res, MessageFormatter.MessageType.MSG));
                if (duelists.size() == 2) {
                    res = "当前桌位人数已满";
                    break;
                }
                if (duelists.size() != 0) {
                    int flag = 0;
                    for (DuelPlayer duelist : duelists) {
                        if (duelist.id.equals(getId(conn))) {
                            res = "您已加入";
                            flag++;
                            break;
                        }
                    }
                    if (flag != 0) {
                        break;
                    }
                }
                DuelPlayer dp = new DuelPlayer("DuelistA", getId(conn), conn);
                dp.LoadDefault();
                broadcast(MessageFormatter.format(getId(conn) + "加入了桌位", MessageFormatter.MessageType.MSG));
                //更新数据
                duelists.add(dp);
                broadcast(MessageFormatter.format(dp.getNetPacket(), MessageFormatter.MessageType.JOIN));
                res = "加入成功";
            }
            /**
             *  operate inside duel
             */
            case OPERATE -> {
                switch (formatMsg.zone) {
                    case "SUMMON" -> {
                        for(int i=0;i<mainGame.dField.summonable_cards.size();i++){
                            if(mainGame.dField.summonable_cards.get(i).uuid.equals(formatMsg.data.toString())){
                                mainGame.dField.ClearCommandFlag();
                                DuelClient.SetResponseI(i << 16);
                                DuelClient.SendResponse();
                            }
                        }
                    }
                    case "QUERY"->{
                        byte[] buffer = new byte[0x2000];
                        int nums =  OCGDll.INSTANCE.query_field_card(pduel,0,LOCATION_HAND,0x1,buffer,0);
                        mainGame.dField.UpdateFieldCard(0,LOCATION_HAND,buffer);
                        System.out.println("手牌数="+nums);
                    }

                }
            }
            case STATE -> {
                switch (formatMsg.zone) {
                    case "hand" -> {
                        conn.send(MessageFormatter.format(mainGame.dField.hand, MessageFormatter.MessageType.STATE, null, "hand"));
                    }
                    default -> {
                        conn.send(MessageFormatter.format(mainGame.dField, MessageFormatter.MessageType.STATE, null, "field"));
                    }
                }
            }
            case DECLARE -> {
                switch (String.valueOf(formatMsg.data)) {
                    case "surrender" -> {
                    }
                    case "duel" -> {
                        if (duelists.size() == 2) {
                            duel.JoinGame(duelists.get(0));
                            duel.JoinGame(duelists.get(1));
                            duel.UpdateDeck();
                            duel.StartDuel();
                            res = "duel!";
                            //TODO 启动duel
                            duel.TPResult();
                            pduel = DuelMode.pduel;
                        } else {
                            res = "房间人数不齐";
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * 房间内广播
     *
     * @param data
     * @param target
     * @param type
     * @param domain
     */
    public void Broadcast(String data, MessageFormatter.MessageType type, String target, String domain) {
        broadcast(MessageFormatter.format(data, type, target, domain));
    }

    /**
     * 桌位广播
     *
     * @param data
     * @param type
     * @param target
     * @param domain
     */
    public void BroadcastToDuelist(String data, MessageFormatter.MessageType type, String target, String domain) {
        for (DuelPlayer duelist : duelists) {
            duelist.conn.send(MessageFormatter.format(data, type, null, domain));
        }
    }

    public void BroadcastToDuelist(String data, MessageFormatter.MessageType type) {
        for (DuelPlayer duelist : duelists) {
            duelist.conn.send(MessageFormatter.format(data, type, null, null));
        }
    }

}
