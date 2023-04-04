package com.ocg.Server;

import com.ocg.DuelMode;
import com.ocg.DuelPlayer;
import com.ocg.Game;
import com.ocg.SingleDuel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SimpleServer {

    public static Game mainGame;
    public static SingleDuel duel;


    public static String MsgAnalyze(String msg,Socket socket) {
        msg = msg.substring(0,msg.length()-4);
        String res = "echo-"+msg;
        // 通用指令集
        if (msg.startsWith("ygo")) {
            int cmd = msg.indexOf('-');
            if (cmd != -1) {
                msg = msg.substring(cmd + 1);
                switch (msg.trim()) {
                    case "h", "help" -> {
                        res = "-help 帮助\n-check 查看房间情况\n-join 加入房间\n-duel 开始决斗";
                        break;
                    }
                    case "check"->{
                        res = "当前玩家情况:";
                        int count = 0;
                        String[] client = new String[2];
                        if(SingleDuel.players[0]!=null){
                            count++;
                            client[0] = SingleDuel.players[0].ip;
                        }
                        if(SingleDuel.players[1]!=null){
                            count++;
                            client[1] = SingleDuel.players[0].ip;
                        }
                        res+=count+"/2";
                        if(client[0]!=null){
                            res+="\n:DuelistA ip ="+client[0];
                        }
                        if(client[1]!=null){
                            res+="\n:DuelistB ip ="+client[1];
                        }
                        break;
                    }
                    case "join" ->{
                        res= "尝试加入房间...\n";
                        if(SingleDuel.players[0]!=null){
                            if(SingleDuel.players[0].ip.equals(socket.getInetAddress().toString())){
                                res = "您已加入当前房间";
                                break;
                            }
                        }
                        if(SingleDuel.players[1]!=null){
                            if(SingleDuel.players[1].ip.equals(socket.getInetAddress().toString())){
                                res = "您已加入当前房间";
                                break;
                            }
                        }
                        if(SingleDuel.players[0]!=null  && SingleDuel.players[1]!=null){
                            res = "当前房间人数已满";
                            break;
                        }
                        DuelPlayer dp = new DuelPlayer("DuelistA",socket.getInetAddress().toString());
                        dp.LoadDefault();
                        if(duel!=null){
                            res+=duel.JoinGame(dp);
                        }
                        break;
                    }
                    case "duel"->{
                        if(SingleDuel.players[0]!=null && SingleDuel.players[1]!=null){
                            SingleDuel.players[0].LoadDefault();
                            SingleDuel.players[1].LoadDefault();
                            duel.UpdateDeck();
                            duel.StartDuel();
                            //TODO 启动duel
                        }else{
                            res = "房间人数不齐";
                        }
                        break;
                    }
                    default -> {
                        res="未知指令";
                    }
                }
            }else{
                res = "未知指令";
            }
        }
        // Duel指令集
        if(msg.startsWith("ocg")){
            int cmd = msg.indexOf('-');
            if (cmd != -1) {
                msg = msg.substring(cmd + 1);
                switch (msg.trim()) {
                    case "surrender"->{
                        break;
                    }
                }
            }
        }
        return res;
    }

    public static void RunSocketServer(Game game, SingleDuel duel_mode) {
        mainGame = game;
        duel = duel_mode;
        try {
            ServerSocket server = new ServerSocket(8888);
            System.out.println("启动服务器....");
            while (true) {
                Socket socket = server.accept();
                System.out.println("客户端:" + socket.getInetAddress().getLocalHost() + "已连接到服务器");
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        InputStream inputStream = socket.getInputStream();
                                        byte[] b = new byte[1024];
                                        int len;
                                        StringBuffer sb = new StringBuffer();
                                        //一次交互完成后，while循环过来，在此阻塞，即监听
                                        while ((len = inputStream.read(b)) != -1) {
                                            sb.append(new String(b, 0, len));
                                            //单次交互结束标识，跳出监听
                                            if (new String(b, 0, len).indexOf("over") >= 0) {
                                                break;
                                            }
                                        }
                                        String content = sb.toString();
                                        System.out.println("接收到客户端消息" + content.substring(0, content.length() - 4));

                                        //往客户端发送数据
                                        String nowTime = (new Date()).toString();
                                        socket.getOutputStream().write((MsgAnalyze(content,socket) + "over").getBytes("UTF-8"));
                                        socket.getOutputStream().flush();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}