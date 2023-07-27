package com.ocg.Moment;

import com.ocg.Moment.Client.ClientInterface;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.InputInterface.InputInterface;
import com.ocg.Moment.Network.NetworkBase;
import com.ocg.Moment.OutputInterface.OutputInterface;
import com.ocg.Moment.Server.LogicServer;

import java.util.ArrayList;


/**
 * Moment内置一个Server
 */
public class Moment {
    private LogicServer logicServer;
    public ArrayList<LogicClient> clients;
    public ArrayList<Room> rooms;

    private MomentInterface momentInterface;
    private Class<ClientInterface> client;
    private Class<NetworkBase> network;
    private Class<InputInterface> input;
    private Class<OutputInterface> output;

    public Moment(Class client, Class network) {
        this.client = client;
        this.network = network;
        clients = new ArrayList<>();
    }
    /*
    暴露方法
     */
    public void startServer() {
        this.logicServer = new LogicServer();
    }

    public Room createRoom(String pass) {
        Room room = new Room(pass);
        rooms.add(room);
        return room;
    }

    public Room matchRoom(String pass) {
        Room target;
        for (int i = 0; i < rooms.size(); i++) {
            target = rooms.get(i);
            if (target.pass.equals(pass)) {
                return target;
            }
        }
        return null;
    }

    /**
     * 新建客户端
     *
     * @param name
     * @return
     */
    public LogicClient newClient(String name) {
        if (network == null || client == null) return null;
        LogicClient client;
        client = new LogicClient(name, this.client, network);
        clients.add(client);
        return client;
    }
}
