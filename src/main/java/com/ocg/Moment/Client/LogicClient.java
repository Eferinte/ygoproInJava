package com.ocg.Moment.Client;

import com.ocg.ChainInfo;
import com.ocg.Client.ClientCard;
import com.ocg.Client.Zone;
import com.ocg.Deck;
import com.ocg.Game;
import com.ocg.Moment.Network.NetworkBase;
import com.ocg.Moment.Network.proStructs.CTOSDeck;
import com.ocg.Moment.Network.proStructs.CTOSJoinGame;
import com.ocg.Moment.Network.proStructs.CTOSPlayerInfo;
import com.ocg.Moment.Network.proStructs.Structs;
import com.ocg.dataController.CardString;
import com.ocg.dataController.DataManager;
import com.ocg.dataController.DeckReader;
import com.ocg.utils.BitException;
import com.ocg.utils.BitWriter;
import com.ocg.utils.ConstantDict.Dictionary;
import com.ocg.utils.ConstantDict.LOCATION;
import com.ocg.utils.Convertor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ocg.Constants.*;

/**
 * Client不能脱离于房间存在
 */
public class LogicClient {
    String name;
    boolean connectStatus = false;
    ClientInterface clientMove;
    NetworkBase network;
    public static Game mainGame = new Game();
    static int response_len = 0;
    static byte[] response_buf = new byte[64];
    byte[] event_string = new byte[256];
    int select_hint = 0;
    int select_unselect_hint = 0;
    int last_select_hint = 0;

    public LogicClient(String name, Class<ClientInterface> client, Class<NetworkBase> network) {
        this.name = name;
        try {
            this.clientMove = client.getDeclaredConstructor(LogicClient.class).newInstance(this);
            this.network = network.getDeclaredConstructor(LogicClient.class).newInstance(this);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 内部方法
     */
    private boolean sendToServer(byte proto, byte[] msg) throws IOException {
        byte[] results = new byte[msg.length + 3];
        short len = (short) (results.length - 2);
        results[0] = (byte) (len & 0xf);
        results[1] = (byte) (len >> 8 & 0xf);
        results[2] = proto;
        System.arraycopy(msg, 0, results, 3, msg.length);
        return network.CTOS(results);
    }

    private boolean sendToServer(byte proto) throws IOException {
        byte[] results = new byte[] { 0x01, 0x00, proto };
        return network.CTOS(results);
    }

    private boolean sendToServer(byte proto, Structs st) throws IOException {
        byte[] stBytes = st.getBytes();
        byte[] results = new byte[stBytes.length + 3];
        if (proto == CTOS_PLAYER_INFO) {
            results[0] = 0x29;
            results[1] = 0x0;
        }
        if (proto == CTOS_JOIN_GAME) {
            results[0] = 0x31;
            results[1] = 0x0;
        }
        if (proto == CTOS_UPDATE_DECK) {
            short deckSize = (short) (stBytes.length + 1);
            results[0] = (byte) (deckSize & 0xFF);
            results[1] = (byte) (deckSize >> 8 & 0xFF);
        }
        results[2] = proto;
        System.arraycopy(stBytes, 0, results, 3, stBytes.length);
        return network.CTOS(results);
    }

    public void join(String passwd) {

    }

    public void startDuel() throws IOException {
        sendToServer(CTOS_HS_START);
    }

    /**
     * 连接pro233服务器
     *
     * @param pass
     */
    public void join233(String pass) throws IOException {
        sendToServer(CTOS_PLAYER_INFO, new CTOSPlayerInfo(this.name));
        sendToServer(CTOS_JOIN_GAME, new CTOSJoinGame((short) 0x1360, 0, pass, this.name));
    }

    public void updateDeck(String deckName) throws IOException {
        Deck deck = DeckReader.ReadYDK(String.format("D:\\game\\ygopro\\deck\\%s.ydk", deckName));
        sendToServer(CTOS_UPDATE_DECK, new CTOSDeck(deck.getMain_code(), deck.getExtra_code(), deck.getSide_code()));
    }

    public void updateDeck() throws IOException {
        Deck deck = DeckReader.ReadYDK(String.format("D:\\game\\ygopro\\deck\\闪刀姬.ydk"));
        sendToServer(CTOS_UPDATE_DECK, new CTOSDeck(deck.getMain_code(), deck.getExtra_code(), deck.getSide_code()));
    }

    public void ready(String deckName) throws IOException {
        updateDeck(deckName);
        sendToServer(CTOS_HS_READY);
    }

    public void ready() throws IOException {
        updateDeck();
        sendToServer(CTOS_HS_READY);
    }

    public void chat(String msg) throws IOException {
        byte[] result = new byte[Math.min(253, msg.length() * 2 + 2)];
        System.arraycopy(msg.getBytes(StandardCharsets.UTF_16LE), 0, result, 0, Math.min(msg.length() * 2, 251));
        result[result.length - 1] = 0x0;
        result[result.length - 2] = 0x0;
        sendToServer(CTOS_CHAT, result);
    }

    /**
     * 直接取随机数
     *
     * @throws IOException
     */
    public void toss() throws IOException {
        byte[] result = new byte[] { (byte) ((int) (Math.random() * 100 % 3) + 1) };
        sendToServer(CTOS_HAND_RESULT, result);
    }

    public void handleSTOC(byte[] buffer, int packetLen) throws IOException {
        ByteBuffer pktData = ByteBuffer.allocate(packetLen).order(ByteOrder.LITTLE_ENDIAN);
        pktData.put(buffer);
        pktData.flip();
        byte pktType = pktData.get();
        switch (pktType) {
            case STOC_GAME_MSG -> {
                byte[] gameMsg = new byte[packetLen - 1];
                pktData.get(gameMsg);
                clientAnalyze(gameMsg, packetLen - 1);
            }
            case STOC_SELECT_HAND -> {
                clientMove.log("猜拳");
                toss();
            }
            case STOC_SELECT_TP -> {
                SelectOption ans = clientMove.select(new ArrayList<SelectOption>(
                        Arrays.asList(new SelectOption("先攻", 1), new SelectOption("后攻", 0))));
                sendToServer(CTOS_TP_RESULT, new byte[] { (byte) ans.value });
            }
            case STOC_HAND_RESULT -> {
                clientMove.log("猜拳结果");
            }
            case STOC_DECK_COUNT -> {
                int deckCount, extraCount, sideCount;
                deckCount = pktData.getShort();
                extraCount = pktData.getShort();
                sideCount = pktData.getShort();
                mainGame.dField.initial(0, deckCount, extraCount);
                deckCount = pktData.getShort();
                extraCount = pktData.getShort();
                sideCount = pktData.getShort();
                mainGame.dField.initial(1, deckCount, extraCount);
            }
            case STOC_DUEL_START -> {
                mainGame.dField.clear();
                mainGame.dInfo.isStarted = true;
                mainGame.dInfo.isFinished = false;
                mainGame.dInfo.lp[0] = 0;
                mainGame.dInfo.lp[1] = 0;
                mainGame.dInfo.turn = 0;
                mainGame.dInfo.time_left[0] = 0;
                mainGame.dInfo.time_left[1] = 0;
                mainGame.dInfo.time_player = 2;

                mainGame.always_chain = true;
                mainGame.ignore_chain = false;
                mainGame.chain_when_avail = false;

            }
            case STOC_DUEL_END -> {
                mainGame.dInfo.isStarted = false;
                mainGame.dInfo.isFinished = true;
                mainGame.is_building = false;
            }
            case STOC_TIME_LIMIT -> {
                byte player = pktData.get();
                short leftTime = pktData.getShort();
                int lPlayer = mainGame.LocalPlayer(player);
                if (lPlayer == 0)
                    sendToServer(CTOS_TIME_CONFIRM);
                mainGame.dInfo.time_player = lPlayer;
                mainGame.dInfo.time_left[lPlayer] = leftTime;
                // clientMove.log("player[" + lPlayer + "] left time =" + leftTime);
            }
            case STOC_CHAT -> {
                String receivedMessage = new String(buffer, 1, packetLen - 1, StandardCharsets.UTF_16LE);
                clientMove.log("[Message]: " + receivedMessage);
            }
        }
        try {
            clientMove.onS2C(pktType, this);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void clientAnalyze(byte[] buffer, int len) {
        ByteBuffer pktData = ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
        pktData.put(buffer);
        pktData.flip();
        int curMsg = Convertor.byteToUInt8(pktData.get());
        mainGame.dInfo.curMsg = curMsg;
        if (curMsg != MSG_RETRY) {
            System.arraycopy(buffer, 0, network.lastSuccessfulMsg, 0, len);
            network.lastSuccessfulMsgLen = len;
        }
        // clientMove.log("[LOG]-curMsg=" + curMsg);
        switch (curMsg) {
            case MSG_RETRY -> {
                clientMove.log("Try again");
            }
            case MSG_HINT -> {
                int type = pktData.get();
                int player = pktData.get();
                int data = pktData.getInt();
                switch (type) {
                    case HINT_EVENT, HINT_MESSAGE -> {
                        clientMove.log(DataManager.getDesc(data));
                    }
                    case HINT_SELECTMSG -> {
                        select_hint = data;
                        last_select_hint = data;
                    }
                    case HINT_OPSELECTED -> {
                        clientMove.log(Convertor.stringFormatterC(
                                DataManager.getSysString(1510),
                                DataManager.getDesc(data)));
                    }
                    case HINT_EFFECT -> {
                        clientMove.log(DataManager.getCardDesc(data) + "的效果");
                    }
                    case HINT_RACE -> {

                    }
                    case HINT_ATTRIB -> {

                    }
                    case HINT_CODE -> {

                    }
                    case HINT_NUMBER -> {

                    }
                    case HINT_CARD -> {

                    }
                    case HINT_ZONE -> {

                    }
                }
            }
            case MSG_WAITING -> {
                clientMove.log("等待对方...");
            }
            case MSG_START -> {
                mainGame.dField.clear();
                byte playerType = pktData.get();
                mainGame.dInfo.isFirst = ((playerType & 0xf) == 0) ? true : false;
                mainGame.dInfo.duel_rule = pktData.get();
                mainGame.dInfo.lp[mainGame.LocalPlayer(0)] = pktData.getInt();
                mainGame.dInfo.lp[mainGame.LocalPlayer(1)] = pktData.getInt();
                int deckCount = pktData.getShort();
                int extraCount = pktData.getShort();
                mainGame.dField.initial(mainGame.LocalPlayer(0), deckCount, extraCount);
                deckCount = pktData.getShort();
                extraCount = pktData.getShort();
                mainGame.dField.initial(mainGame.LocalPlayer(1), deckCount, extraCount);
                mainGame.dInfo.turn = 0;
                mainGame.dInfo.is_shuffling = false;
                network.lastSuccessfulMsgLen = 0;
            }
            case MSG_UPDATE_DATA -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int location = pktData.get();
                mainGame.dField.updateFieldCard(player, location, Convertor.getRestBytes(pktData));
            }
            case MSG_UPDATE_CARD -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int loc = pktData.get();
                int seq = pktData.get();
                pktData.getInt();// 代替updateCard中+4
                mainGame.dField.updateCard(player, loc, seq, Convertor.getRestBytes(pktData));
            }
            case MSG_SELECT_BATTLECMD -> {
                pktData.get();
                int code, desc, count, con, loc, seq;
                ClientCard pCard;
                mainGame.dField.activatable_cards.clear();
                mainGame.dField.activatable_descs.clear();
                mainGame.dField.conti_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    desc = pktData.getInt();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    int flag = 0;
                    if ((code & 0x80000000) != 0) {
                        flag = EDESC_OPERATION;
                        code &= 0x7fffffff;
                    }
                    mainGame.dField.activatable_cards.add(pCard);
                    Map<Integer, Integer> map = new HashMap<>();
                    map.put(desc, flag);
                    mainGame.dField.activatable_descs.add(map);
                    if (flag == EDESC_OPERATION) {
                        pCard.chain_code = code;
                        mainGame.dField.conti_cards.add(pCard);
                        mainGame.dField.conti_act = true;
                    } else {
                        pCard.cmdFlag |= COMMAND_ACTIVATE;
                        if (pCard.controller == 0) {
                            if (pCard.location == LOCATION_GRAVE)
                                mainGame.dField.grave_act = true;
                            else if (pCard.location == LOCATION_REMOVED)
                                mainGame.dField.remove_act = true;
                            else if (pCard.location == LOCATION_EXTRA)
                                mainGame.dField.extra_act = true;
                        }
                    }
                }
                mainGame.dField.attackable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.attackable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_ATTACK;
                }
                if (pktData.get() != 0) {
                    mainGame.btnM2 = true;
                }
                if (pktData.get() != 0) {
                    mainGame.btnEP = true;
                }
                clientMove.log("请选择操作");
            }
            case MSG_SELECT_IDLECMD -> {
                pktData.get();
                int code, desc, count, con, loc, seq;
                ClientCard pCard;
                mainGame.dField.summonable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.summonable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_SUMMON;
                }
                mainGame.dField.spsummonable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.spsummonable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_SPSUMMON;
                    if (pCard.location == LOCATION_DECK) {
                        pCard.setCode(code);
                        mainGame.dField.deck_act = true;
                    } else if (pCard.location == LOCATION_GRAVE) {
                        mainGame.dField.grave_act = true;
                    } else if (pCard.location == LOCATION_REMOVED) {
                        mainGame.dField.remove_act = true;
                    } else if (pCard.location == LOCATION_EXTRA) {
                        mainGame.dField.extra_act = true;
                    } else {
                        seq = mainGame.dInfo.duel_rule >= 4 ? 0 : 6;
                        if (pCard.location == LOCATION_SZONE && pCard.sequence == seq
                                && ((pCard.type & TYPE_PENDULUM) != 0) && pCard.equipTarget == null) {
                            mainGame.dField.pzone_act[pCard.controller] = true;
                        }
                    }
                }
                mainGame.dField.reposable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.reposable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_REPOS;
                }
                mainGame.dField.msetable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.msetable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_MSET;
                }
                mainGame.dField.ssetable_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    mainGame.dField.ssetable_cards.add(pCard);
                    pCard.cmdFlag |= COMMAND_SSET;
                }
                mainGame.dField.activatable_cards.clear();
                mainGame.dField.activatable_descs.clear();
                mainGame.dField.conti_cards.clear();
                count = pktData.get();
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    con = mainGame.LocalPlayer(pktData.get());
                    loc = pktData.get();
                    seq = pktData.get();
                    desc = pktData.getInt();
                    pCard = mainGame.dField.getCard(con, loc, seq);
                    int flag = 0;
                    if ((code & 0x80000000) != 0) {
                        flag = EDESC_OPERATION;
                        code &= 0x7fffffff;
                    }
                    mainGame.dField.activatable_cards.add(pCard);
                    Map pair = new HashMap();
                    pair.put(desc, flag);
                    mainGame.dField.activatable_descs.add(pair);
                    if (flag == EDESC_OPERATION) {
                        pCard.chain_code = code;
                        mainGame.dField.conti_cards.add(pCard);
                        mainGame.dField.conti_act = true;
                    } else {
                        pCard.cmdFlag |= COMMAND_ACTIVATE;
                        if (pCard.controller == 0) {
                            if (pCard.location == LOCATION_GRAVE)
                                mainGame.dField.grave_act = true;
                            else if (pCard.location == LOCATION_REMOVED)
                                mainGame.dField.remove_act = true;
                            else if (pCard.location == LOCATION_EXTRA)
                                mainGame.dField.extra_act = true;
                        }
                    }
                }
                if (pktData.get() != 0) {
                    // TODO 允许进入BP
                    mainGame.btnBP = true;
                }
                if (pktData.get() != 0) {
                    // TODO 允许进入EP
                    mainGame.btnEP = true;
                }
                if (pktData.get() != 0) {
                    // TODO 允许洗切手卡
                } else {
                }
                clientMove.log("请选择操作");

            }
            case MSG_SELECT_EFFECTYN -> {
                pktData.get();
                int code = pktData.getInt();
                int c = mainGame.LocalPlayer(pktData.get());
                int l = pktData.get();
                int s = pktData.get();
                ClientCard pCard = mainGame.dField.getCard(c, l, s);
                if (pCard.code != code)
                    pCard.setCode(code);
                pktData.get();
                if (l != LOCATION_DECK) {
                    pCard.is_highlighting = true;
                    mainGame.dField.highlighting_card = pCard;
                }
                int desc = pktData.getInt();
                if (desc == 0) {
                    clientMove.log(Convertor.stringFormatterC(DataManager.getSysString(200),
                            DataManager.formatLocation(l, s), DataManager.getCardDesc(code).name));
                } else if (desc == 221) {
                    clientMove.log(Convertor.stringFormatterC(DataManager.getSysString(221),
                            DataManager.formatLocation(l, s), DataManager.getCardDesc(code).name));
                } else if (desc < 2048) {
                    clientMove.log(Convertor.stringFormatterC(DataManager.getSysString(desc),
                            DataManager.getCardDesc(code).name));
                } else {
                    clientMove.log(
                            Convertor.stringFormatterC(DataManager.getDesc(desc), DataManager.getCardDesc(code).name));
                }
                SelectOption ans = clientMove.select(SelectOption.getOptions(new ArrayList<String>() {
                    {
                        add("否");
                        add("是");
                    }
                }));
                setResponseI(ans.value);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_YESNO -> {
                pktData.get();
                int desc = pktData.getInt();
                mainGame.dField.highlighting_card = null;
                clientMove.log(DataManager.getDesc(desc));
                SelectOption ans = clientMove.select(SelectOption.getOptions(new ArrayList<String>() {
                    {
                        add("否");
                        add("是");
                    }
                }));
                setResponseI(ans.value);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_OPTION -> {
                // 非原生处理
                pktData.get();
                int count = pktData.get();
                ArrayList<SelectOption> opts = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    opts.add(new SelectOption(DataManager.getSysString(pktData.getInt()), i));
                }
                SelectOption ans = clientMove.select(opts);
                setResponseI(ans.value);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_CARD -> {
                pktData.get();// player
                mainGame.dField.select_cancelable = pktData.get() != 0;
                mainGame.dField.select_min = pktData.get();
                mainGame.dField.select_max = pktData.get();
                int count = pktData.get();
                mainGame.dField.selectable_cards.clear();
                mainGame.dField.selected_cards.clear();
                int c, l, s, ss, code;
                long[] hand_count = new long[] { mainGame.dField.hand[0].size(), mainGame.dField.hand[1].size() };
                int[] select_count_in_hand = new int[] { 0, 0 };
                boolean select_ready = mainGame.dField.select_min == 0;
                mainGame.dField.select_ready = select_ready;
                ClientCard pCard;
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    ss = pktData.get();
                    if ((l & LOCATION_OVERLAY) > 0) {
                        pCard = mainGame.dField.getCard(c, l & 0x7f, s).overlayed.get(ss);
                    } else
                        pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    pCard.select_seq = i;
                    pCard.is_selectable = true;
                    pCard.is_selected = false;
                    mainGame.dField.selectable_cards.add(pCard);
                    // TODO panelMode ???
                }
                // TODO 排序
                if (select_hint != 0)
                    clientMove.log(String.format("%s(%d-%d)", DataManager.getDesc(select_hint),
                            mainGame.dField.select_min, mainGame.dField.select_max));
                else
                    clientMove.log(String.format("%s(%d-%d)", DataManager.getData(560), mainGame.dField.select_min,
                            mainGame.dField.select_max));
                select_hint = 0;
                // 选择
                // TODO 接口化到client中
                while (mainGame.dField.selected_cards.size() < mainGame.dField.select_max) {
                    ArrayList<String> arrayList = Convertor.getStringList(mainGame.dField.selectable_cards, (cards) -> {
                        if (cards.is_selected)
                            return false;
                        return true;
                    });
                    SelectOption ans = clientMove.select(SelectOption.getOptions(arrayList));
                    mainGame.dField.selected_cards.add(mainGame.dField.selectable_cards.get(ans.value));
                    if (mainGame.dField.selected_cards.size() >= mainGame.dField.select_min
                            && mainGame.dField.select_min != mainGame.dField.select_max) {
                        clientMove.log("已满足最小选择，是否确定？");
                        ans = clientMove.select(SelectOption.getOptions(new ArrayList<String>() {
                            {
                                add("结束选择");
                                add("继续");
                            }
                        }));
                        if (ans.value == 0)
                            break;
                    }
                }
                mainGame.dField.setResponseSelectedCards();
                try {
                    sendResponse();
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_CHAIN -> {
                int player = pktData.get();
                int count = pktData.get();
                int speCount = pktData.get();
                int forced = pktData.get();
                int hint0 = pktData.getInt();
                int hint1 = pktData.getInt();
                int code, c, l, s, ss, desc;
                ClientCard pCard;
                boolean panelMode = false;
                boolean conti_exist = false;
                boolean select_trigger = (speCount == 0x7f);
                mainGame.dField.chain_forced = (forced != 0);
                mainGame.dField.activatable_cards.clear();
                mainGame.dField.activatable_descs.clear();
                mainGame.dField.conti_cards.clear();
                for (int i = 0; i < count; i++) {
                    int flag = pktData.get();
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    ss = pktData.get();
                    desc = pktData.getInt();
                    pCard = mainGame.dField.getCard(c, l, s, ss);
                    mainGame.dField.activatable_cards.add(pCard);
                    HashMap<Integer, Integer> m = new HashMap<>();
                    m.put(desc, flag);
                    mainGame.dField.activatable_descs.add(m);
                    pCard.is_selected = false;
                    if (flag == EDESC_OPERATION) {
                        pCard.chain_code = code;
                        mainGame.dField.conti_cards.add(pCard);
                        mainGame.dField.conti_act = true;
                        conti_exist = true;
                    } else {
                        pCard.is_selected = true;
                        if (flag == EDESC_RESET) {
                            pCard.cmdFlag |= COMMAND_RESET;
                        } else {
                            pCard.cmdFlag |= COMMAND_ACTIVATE;
                        }
                        if (pCard.location == LOCATION_DECK) {
                            pCard.setCode(code);
                            mainGame.dField.deck_act = true;
                        } else if (l == LOCATION_GRAVE) {
                            mainGame.dField.grave_act = true;
                        } else if (l == LOCATION_REMOVED) {
                            mainGame.dField.remove_act = true;
                        } else if (l == LOCATION_EXTRA) {
                            mainGame.dField.extra_act = true;
                        } else if (l == LOCATION_OVERLAY) {
                            panelMode = true;
                        }
                    }
                }
                clientMove.log("可发动的卡 =" + mainGame.dField.activatable_cards);
                if (!select_trigger && (forced == 0)
                        && (mainGame.ignore_chain || ((count == 0 || speCount == 0) && !mainGame.always_chain)
                                && (count == 0 || !mainGame.chain_when_avail))) {
                    try {
                        setResponseI(-1);
                        mainGame.dField.clearChainSelect();
                        sendResponse();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                if ((forced != 0) && !(mainGame.always_chain || mainGame.chain_when_avail)) {
                    try {
                        setResponseI(0);
                        mainGame.dField.clearChainSelect();
                        sendResponse();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                try {
                    if (forced == 0) {
                        if (count == 0) {
                            clientMove.log(String.format("%s\n%s",
                                    DataManager.getSysString(201),
                                    DataManager.getSysString(202)));
                            setResponseI(-1);
                            sendResponse();
                        } else if (select_trigger) {
                            clientMove.log(String.format("%s\n%s",
                                    DataManager.getSysString(222),
                                    DataManager.getSysString(223)));
                            ArrayList options = new ArrayList<SelectOption>(2);
                            options.add(new SelectOption("连锁", 0));
                            options.add(new SelectOption("不连锁", 1));
                            SelectOption ans = clientMove.select(options);
                            clientMove.log("选择了" + ans);
                            if (ans.value == 1)
                                setResponseI(-1);
                            else {
                                clientMove.log("请选择要发动的效果");
                                ans = clientMove.select(SelectOption
                                        .getOptions(Convertor.getStringList(mainGame.dField.activatable_cards)));
                                setResponseI(ans.value);
                            }
                            sendResponse();
                        } else {
                            clientMove.log(DataManager.getSysString(203));
                            ArrayList options = new ArrayList<SelectOption>(2);
                            options.add(new SelectOption("连锁", 0));
                            options.add(new SelectOption("不连锁", 1));
                            SelectOption ans = clientMove.select(options);
                            clientMove.log("选择了" + ans);
                            if (ans.value == 1)
                                setResponseI(-1);
                            else {
                                clientMove.log("请选择要发动的效果");
                                ans = clientMove.select(SelectOption
                                        .getOptions(Convertor.getStringList(mainGame.dField.activatable_cards)));
                                setResponseI(ans.value);
                            }
                            sendResponse();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_PLACE, MSG_SELECT_DISFIELD -> {
                int selecting_player = pktData.get();
                int count = pktData.get();
                mainGame.dField.select_min = count > 0 ? count : 1;
                mainGame.dField.select_ready = false;
                mainGame.dField.select_cancelable = count == 0;
                mainGame.dField.selectable_field = pktData.getInt();
                if (selecting_player == mainGame.LocalPlayer(1))
                    mainGame.dField.selectable_field = (mainGame.dField.selectable_field >> 16)
                            | (mainGame.dField.selectable_field << 16);
                mainGame.dField.selected_field = 0;
                byte[] respBuf = new byte[64];
                int pZone = 0;
                if (mainGame.dInfo.curMsg == MSG_SELECT_PLACE) {
                    // TODO selectable flag 如何表示可选位置
                    // 此处仅是主要怪兽区的选择
                    SelectOption ans = clientMove.select(Zone.getSelectablePlace(mainGame.dField.selectable_field));
                    respBuf[0] = (byte) selecting_player;
                    respBuf[1] = (byte) Dictionary.locationMap.get(Zone.getType(ans.value)).value;
                    respBuf[2] = (byte) Zone.getSeq(ans.value);
                    setResponseB(respBuf, 3);
                    try {
                        sendResponse();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                }
            }
            case MSG_SELECT_POSITION -> {
                pktData.get();
                int code = pktData.getInt();
                int positions = pktData.get();
                if (positions == POS_ATTACK || positions == POS_FACEDOWN_ATTACK || positions == POS_FACEUP_DEFENSE
                        || positions == POS_FACEDOWN_DEFENSE) {
                    setResponseI(positions);
                    return;
                }
                try {
                    ArrayList opts = new ArrayList();
                    if ((positions & POS_ATTACK) != 0)
                        opts.add(new SelectOption("表侧攻击表示", 0));
                    if ((positions & POS_FACEUP_DEFENSE) != 0)
                        opts.add(new SelectOption("表侧守备表示", 1));
                    if ((positions & POS_FACEDOWN_DEFENSE) != 0)
                        opts.add(new SelectOption("里侧守备表示", 2));
                    SelectOption ans = clientMove.select(opts);
                    switch (ans.value) {
                        case 0 -> setResponseI(POS_FACEUP_ATTACK);
                        case 1 -> setResponseI(POS_FACEUP_DEFENSE);
                        case 2 -> setResponseI(POS_FACEDOWN_DEFENSE);
                    }
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case MSG_SELECT_TRIBUTE -> {
                pktData.get();
                mainGame.dField.select_cancelable = pktData.get() != 0;
                mainGame.dField.select_min = pktData.get();
                mainGame.dField.select_max = pktData.get();
                int count = pktData.get();
                mainGame.dField.selectable_cards.clear();
                mainGame.dField.selected_cards.clear();
                mainGame.dField.selectsum_all.clear();
                mainGame.dField.selectsum_cards.clear();
                for (int i = 0; i < count; i++) {
                    int code = pktData.getInt();
                    int c = mainGame.LocalPlayer(pktData.get());
                    int l = pktData.get();
                    int s = pktData.get();
                    int t = pktData.get();
                    ClientCard pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    mainGame.dField.selectable_cards.add(pCard);
                    mainGame.dField.selectsum_all.add(pCard);
                    pCard.opParam = t << 16 | 1;
                    pCard.select_seq = i;
                    pCard.is_selectable = true;
                }
                if (select_hint != 0)
                    clientMove.log(String.format(
                            "%s(%d-%d)",
                            DataManager.getDesc(select_hint),
                            mainGame.dField.select_min,
                            mainGame.dField.select_max));
                else
                    clientMove.log(String.format(
                            "%s(%d-%d)",
                            DataManager.getDesc(531),
                            mainGame.dField.select_min,
                            mainGame.dField.select_max));
                select_hint = 0;
                // handler
                while (!mainGame.dField.checkSelectTribute()) {
                    SelectOption ans = clientMove.select(SelectOption.getOptions(mainGame.dField.selectable_cards));
                    ClientCard card = mainGame.dField.selectable_cards.get(ans.value);
                    if (card.is_selected) {
                        mainGame.dField.selected_cards.remove(card);
                    } else {
                        mainGame.dField.selected_cards.add(card);
                    }
                    card.is_selected = !card.is_selected;
                }
                mainGame.dField.setResponseSelectedCards();
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_SELECT_COUNTER -> {
            }
            case MSG_SELECT_SUM -> {
                mainGame.dField.select_mode = pktData.get();
                pktData.get();
                mainGame.dField.select_sumval = pktData.getInt();
                mainGame.dField.select_min = pktData.get();
                mainGame.dField.select_max = pktData.get();
                mainGame.dField.must_select_count = pktData.get();
                mainGame.dField.selectsum_all.clear();
                mainGame.dField.selected_cards.clear();
                mainGame.dField.selectsum_cards.clear();
                for (int i = 0; i < mainGame.dField.must_select_count; i++) {
                    int code = pktData.getInt();
                    int c = mainGame.LocalPlayer(pktData.get());
                    int l = pktData.get();
                    int s = pktData.get();
                    ClientCard pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    pCard.opParam = pktData.getInt();
                    pCard.select_seq = 0;
                    mainGame.dField.selected_cards.add(pCard);
                }
                int count = pktData.get();
                for (int i = 0; i < count; i++) {
                    int code = pktData.getInt();
                    int c = mainGame.LocalPlayer(pktData.get());
                    int l = pktData.get();
                    int s = pktData.get();
                    ClientCard pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    pCard.opParam = pktData.getInt();
                    pCard.select_seq = i;
                    mainGame.dField.selectsum_all.add(pCard);
                }
                if (select_hint != 0)
                    clientMove.log(String.format(
                            "%s(%d)",
                            DataManager.getDesc(select_hint),
                            mainGame.dField.select_sumval));
                else
                    clientMove.log(String.format(
                            "%s(%d)",
                            DataManager.getDesc(560),
                            mainGame.dField.select_sumval));
                select_hint = 0;
                // handler
                mainGame.dField.showSelectSum(this, clientMove);

            }
            case MSG_SORT_CARD -> {
                pktData.get();
                int count = pktData.get();
                mainGame.dField.selectable_cards.clear();
                mainGame.dField.selected_cards.clear();
                mainGame.dField.sort_list.clear();
                int c, l, s, code;
                ClientCard pCard;
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    mainGame.dField.selectable_cards.add(pCard);
                    mainGame.dField.sort_list.add(0);
                }
                clientMove.log(DataManager.getSysString(205));
                mainGame.dField.select_min = 0;
                mainGame.dField.select_max = count;
                mainGame.dField.showSelectCard();
            }
            case MSG_SELECT_UNSELECT_CARD -> {
                pktData.get();
                boolean finish_able = pktData.get() != 0;
                boolean cancel_able = pktData.get() != 0;
                mainGame.dField.select_cancelable = finish_able || cancel_able;
                mainGame.dField.select_min = pktData.get();
                mainGame.dField.select_max = pktData.get();
                int count1 = pktData.get();
                mainGame.dField.selectable_cards.clear();
                mainGame.dField.selected_cards.clear();
                int c, l, s, ss, code;
                long[] hand_count = { mainGame.dField.hand[0].size(), mainGame.dField.hand[1].size() };
                int[] select_count_in_hand = { 0, 0 };
                mainGame.dField.select_ready = false;
                ClientCard pCard;
                for (int i = 0; i < count1; i++) {
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    ss = pktData.get();
                    if ((l & LOCATION_OVERLAY) > 0) {
                        pCard = mainGame.dField.getCard(c, l & 0x7f, s).overlayed.get(ss);
                    } else {
                        pCard = mainGame.dField.getCard(c, l, s);
                    }
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    pCard.select_seq = i;
                    mainGame.dField.selectable_cards.add(pCard);
                    pCard.is_selectable = true;
                    pCard.is_selected = false;
                }
                int count2 = pktData.get();
                for (int i = count1; i < count1 + count2; i++) {
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    ss = pktData.get();
                    if ((l & LOCATION_OVERLAY) > 0) {
                        pCard = mainGame.dField.getCard(c, l & 0x7f, s).overlayed.get(ss);
                    } else {
                        pCard = mainGame.dField.getCard(c, l, s);
                    }
                    if (code != 0 && pCard.code != code)
                        pCard.setCode(code);
                    pCard.select_seq = i;
                    mainGame.dField.selectable_cards.add(pCard);
                    pCard.is_selectable = true;
                    pCard.is_selected = true;
                }
                if (select_hint != 0) {
                    select_unselect_hint = select_hint;
                }
                if (select_unselect_hint != 0) {
                    clientMove.log(String.format("%s(%d-%d)",
                            DataManager.getDesc(select_unselect_hint),
                            mainGame.dField.select_min,
                            mainGame.dField.select_max));
                } else
                    clientMove.log(String.format("%s(%d-%d)",
                            DataManager.getDesc(560),
                            mainGame.dField.select_min,
                            mainGame.dField.select_max));
                select_hint = 0;
                ArrayList<SelectOption> opts = new ArrayList<>();
                opts.add(new SelectOption("取消", 0));
                for (int i = 0; i < mainGame.dField.selectable_cards.size(); i++) {
                    pCard = mainGame.dField.selectable_cards.get(i);
                    if (pCard.is_selected)
                        opts.add(new SelectOption(pCard.toString() + "(已选择)", i + 1));
                    else
                        opts.add(new SelectOption(pCard.toString(), i + 1));
                }
                SelectOption ans = clientMove.select(opts);
                // handleSelect
                if (ans.value == 0) {
                    setResponseI(-1);
                } else {
                    pCard = mainGame.dField.selectable_cards.get(ans.value - 1);
                    pCard.is_selected = !pCard.is_selected;
                    mainGame.dField.selected_cards.add(pCard);
                    mainGame.dField.setResponseSelectedCards();
                }
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_CONFIRM_DECKTOP -> {
                int player = mainGame.LocalPlayer((int) pktData.get());
                int count = pktData.get();
                int code;
                ClientCard pCard;
                mainGame.dField.selectable_cards.clear();
                ArrayList<ClientCard> deck = mainGame.dField.deck[player];
                clientMove.log(DataManager.getDesc(207).toString() + count);
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    pktData.get();
                    pktData.get();
                    pktData.get();
                    pCard = deck.get(deck.size() - i - 1);
                    if (code != 0) {
                        pCard.setCode(code);
                        clientMove.log(DataManager.getCardDesc(code).name);
                    }
                }
            }
            case MSG_CONFIRM_CARDS -> {
                pktData.get();
                int count = pktData.get();
                int code, c, l, s;
                ArrayList<ClientCard> field_confirm;
                ArrayList<ClientCard> panel_confirm;
                ClientCard pCard;
                clientMove.log(DataManager.getSysString(208) + count);
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    c = mainGame.LocalPlayer(pktData.get());
                    l = pktData.get();
                    s = pktData.get();
                    pCard = mainGame.dField.getCard(c, l, s);
                    if (code != 0) {
                        pCard.setCode(code);
                    }
                    clientMove.log(DataManager.getCardDesc(code).name);
                }
            }
            case MSG_SHUFFLE_DECK -> {
                int player = pktData.get();
                clientMove.log("洗切卡组");
            }
            case MSG_SHUFFLE_HAND -> {
                int player = pktData.get();
                int count = pktData.get();
                clientMove.log("洗切手卡");
            }
            case MSG_NEW_TURN -> {
                clientMove.log("Turn-" + mainGame.dInfo.turn);
                mainGame.dInfo.turn++;
            }
            case MSG_NEW_PHASE -> {
                short phase = pktData.getShort();
                mainGame.btnBP = false;
                mainGame.btnM2 = false;
                mainGame.btnEP = false;
                switch (phase) {
                    case PHASE_DRAW -> {
                        clientMove.log("PHASE_DRAW");
                    }
                    case PHASE_STANDBY -> {
                        clientMove.log("PHASE_STANDBY");
                    }
                    case PHASE_MAIN1 -> {
                        clientMove.log("PHASE_MAIN1");
                    }
                    case PHASE_BATTLE_START -> {
                        clientMove.log("PHASE_BATTLE_START");
                    }
                    case PHASE_MAIN2 -> {
                        clientMove.log("PHASE_MAIN2");
                    }
                    case PHASE_END -> {
                        clientMove.log("PHASE_END");
                    }
                }
            }
            case MSG_MOVE -> {
                int code = pktData.getInt();
                int pc = mainGame.LocalPlayer(pktData.get());
                int pl = pktData.get() & 0xff;
                int ps = pktData.get();
                int pp = pktData.get();
                int cc = mainGame.LocalPlayer(pktData.get());
                int cl = pktData.get() & 0xff;
                int cs = pktData.get();
                int cp = pktData.get();
                int reason = pktData.getInt();
                int appear = 12;
                clientMove.log(
                        String.format("%s从%s移动到%s",
                                DataManager.getCardDesc(code),
                                Dictionary.getLocationDesc(pl),
                                Dictionary.getLocationDesc(cl)));
                if (pl == 0) {
                    ClientCard pCard = new ClientCard();
                    pCard.position = cp;
                    pCard.setCode(code);
                    mainGame.dField.addCard(pCard, cc, cl, cs);
                } else if (cl == 0) {
                    ClientCard pCard = mainGame.dField.getCard(pc, pl, ps);
                    if (code != 0 && pCard.code != code) {
                        pCard.setCode(code);
                    }
                    pCard.clearTarget();
                    for (ClientCard clientCard : pCard.equipped) {
                        clientCard.equipTarget = null;
                    }
                    mainGame.dField.removeCard(pc, pl, ps);
                } else {
                    if ((pl & LOCATION_OVERLAY) == 0 && (cl & LOCATION_OVERLAY) == 0) {
                        ClientCard pCard = mainGame.dField.getCard(pc, pl, ps);
                        if (pCard.code != code && (code != 0 || cl == LOCATION_EXTRA)) {
                            pCard.setCode(code);
                        }
                        pCard.cHint = 0;
                        pCard.chValue = 0;
                        if ((pl & LOCATION_ONFIELD) != 0 && cl != pl) {
                            pCard.counters.clear();
                        }
                        if (cl != pl) {
                            pCard.clearTarget();
                            if (pCard.equipTarget != null) {
                                pCard.equipTarget.is_showequip = false;
                                pCard.equipTarget.equipped.remove(pCard);
                                pCard.equipTarget = null;
                            }
                        }
                        pCard.is_hovered = false;
                        pCard.is_showequip = false;
                        pCard.is_showtarget = false;
                        pCard.is_showchaintarget = false;
                        mainGame.dField.removeCard(pc, pl, ps);
                        pCard.position = cp;
                        mainGame.dField.addCard(pCard, cc, cl, cs);
                    } else if ((pl & LOCATION_OVERLAY) == 0) {
                        ClientCard pCard = mainGame.dField.getCard(pc, pl, ps);
                        if (code != 0 && pCard.code != code) {
                            pCard.setCode(code);
                        }
                        pCard.counters.clear();
                        pCard.clearTarget();
                        if (pCard.equipTarget != null) {
                            pCard.equipTarget.is_showequip = false;
                            pCard.equipTarget.equipped.remove(pCard);
                            pCard.equipTarget = null;
                        }
                        pCard.is_showequip = false;
                        pCard.is_showtarget = false;
                        pCard.is_showchaintarget = false;
                        ClientCard olCard = mainGame.dField.getCard(cc, cl & 0x7f, cs);
                        mainGame.dField.removeCard(pc, pl, ps);
                        olCard.overlayed.add(pCard);
                        mainGame.dField.overlay_cards.add(pCard);
                        pCard.overlayTarget = olCard;
                        pCard.location = LOCATION_OVERLAY;
                        pCard.sequence = olCard.overlayed.size() - 1;
                    } else if ((cl & LOCATION_OVERLAY) == 0) {
                        ClientCard olCard = mainGame.dField.getCard(pc, pl & 0x7f, ps);
                        ClientCard pCard = olCard.overlayed.get(pp);
                        olCard.overlayed.remove(pCard);
                        pCard.overlayTarget = null;
                        pCard.position = cp;
                        mainGame.dField.addCard(pCard, cc, cl, cs);
                        mainGame.dField.overlay_cards.remove(pCard);
                        for (int i = 0; i < olCard.overlayed.size(); i++) {
                            olCard.overlayed.get(i).sequence = i;
                        }
                    } else {
                        ClientCard olCard1 = mainGame.dField.getCard(pc, pl & 0x7f, ps);
                        ClientCard pCard = olCard1.overlayed.get(pp);
                        ClientCard olCard2 = mainGame.dField.getCard(cc, cl & 0x7f, cs);
                        olCard1.overlayed.remove(pCard);
                        olCard2.overlayed.add(pCard);
                        pCard.sequence = olCard2.overlayed.size() - 1;
                        pCard.location = LOCATION_OVERLAY;
                        pCard.overlayTarget = olCard2;
                        for (int i = 0; i < olCard1.overlayed.size(); i++) {
                            olCard1.overlayed.get(i).sequence = i;
                        }
                    }
                }
            }
            case MSG_CHAINING -> {
                int code = pktData.getInt();
                int pcc = mainGame.LocalPlayer(pktData.get());
                int pcl = pktData.get();
                int pcs = pktData.get();
                int subs = pktData.get();
                int cc = mainGame.LocalPlayer(pktData.get());
                int cl = pktData.get();
                int cs = pktData.get();
                int desc = pktData.getInt();
                pktData.get();
                ClientCard pcard = mainGame.dField.getCard(pcc, pcl, pcs, subs);
                clientMove.log(Dictionary.getPlayerDesc(pcc) + "发动了" + pcard);
                if (pcard.code != code)
                    pcard.code = code;
                // TODO
                pcard.is_highlighting = true;
                if ((pcard.location & 0x30) != 0) {
                } else {
                }
                pcard.is_highlighting = false;

                mainGame.dField.current_chain.chain_card = pcard;
                mainGame.dField.current_chain.code = code;
                mainGame.dField.current_chain.desc = desc;
                mainGame.dField.current_chain.controller = cc;
                mainGame.dField.current_chain.location = cl;
                mainGame.dField.current_chain.sequence = cs;
                mainGame.dField.current_chain.solved = false;
                mainGame.dField.current_chain.target.clear();

            }
            case MSG_CHAINED -> {
                int ct = pktData.get();
                clientMove.log(DataManager.getSysString(1609)
                        + DataManager.getCardDesc(mainGame.dField.current_chain.code).toString());
                mainGame.dField.chains.add(mainGame.dField.current_chain);
            }
            case MSG_CHAIN_SOLVING -> {
                int ct = pktData.get();
                if (mainGame.dField.chains.size() > 1) {
                    mainGame.dField.chains.get(ct - 1).solved = true;
                    clientMove.log("连锁结算");
                }
                mainGame.dField.last_chain = false;
            }
            case MSG_CHAIN_SOLVED -> {
                pktData.get();
            }
            case MSG_CHAIN_END -> {
                for (ChainInfo chain : mainGame.dField.chains) {
                    for (ClientCard card : chain.target) {
                        card.is_showchaintarget = false;
                    }
                    chain.chain_card.is_showchaintarget = false;
                }
                mainGame.dField.chains.clear();
            }
            case MSG_CHAIN_NEGATED, MSG_CHAIN_DISABLED -> {
                int ct = pktData.get();
                clientMove.log("连锁失效" + DataManager.getData(mainGame.dField.chains.get(ct - 1).code));
            }
            case MSG_DRAW -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int count = pktData.get();
                ClientCard pCard;
                StringBuilder log = new StringBuilder("");
                // 设置code
                if (player == 0) {
                    log.append("我方抽了" + count + "张");
                } else {
                    log.append("对方抽了" + count + "张");
                }
                for (int i = 0; i < count; i++) {
                    int code = pktData.getInt();
                    pCard = mainGame.dField.getCard(player, LOCATION_DECK, mainGame.dField.deck[player].size() - 1 - i);
                    if (!mainGame.dField.deck_reversed || code != 0) {
                        pCard.setCode(code & 0x7fffffff);
                        log.append(DataManager.getCardDesc(pCard.code) + ",");
                    }
                }
                clientMove.log(log.toString());
                for (int i = 0; i < count; ++i) {
                    pCard = mainGame.dField.getCard(player, LOCATION_DECK, mainGame.dField.deck[player].size() - 1);
                    mainGame.dField.deck[player].remove(mainGame.dField.deck[player].size() - 1);
                    mainGame.dField.addCard(pCard, player, LOCATION_HAND, 0);
                }
            }
            case MSG_DAMAGE -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int val = pktData.getInt();
                int finalLp = mainGame.dInfo.lp[player] - val;
                if (finalLp < 0)
                    finalLp = 0;
                if (player == 0)
                    clientMove.log(Convertor.stringFormatterC(
                            DataManager.getSysString(1613),
                            val));
                else
                    clientMove.log(Convertor.stringFormatterC(
                            DataManager.getSysString(1614),
                            val));
                mainGame.dInfo.lp[player] = finalLp;
                clientMove.log(String.format("LP = %d", mainGame.dInfo.lp[player]));
            }
            case MSG_RECOVER -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int val = pktData.getInt();
                int finalLp = mainGame.dInfo.lp[player] + val;
                if (finalLp < 0)
                    finalLp = 0;
                if (player == 0)
                    clientMove.log(Convertor.stringFormatterC(
                            DataManager.getSysString(1615),
                            val));
                else
                    clientMove.log(Convertor.stringFormatterC(
                            DataManager.getSysString(1616),
                            val));
                mainGame.dInfo.lp[player] = finalLp;
                clientMove.log(String.format("LP = %d", mainGame.dInfo.lp[player]));
            }
            case MSG_EQUIP -> {
                int c1 = mainGame.LocalPlayer(pktData.get());
                int l1 = pktData.get();
                int s1 = pktData.get();
                pktData.get();
                int c2 = mainGame.LocalPlayer(pktData.get());
                int l2 = pktData.get();
                int s2 = pktData.get();
                pktData.get();
                ClientCard pC1 = mainGame.dField.getCard(c1, l1, s1);
                ClientCard pC2 = mainGame.dField.getCard(c2, l2, s2);
                if (pC1.equipTarget != null) {
                    pC1.equipTarget.equipped.remove(pC1);
                }
                pC1.equipTarget = pC2;
                pC2.equipped.add(pC1);
                //
                clientMove.log(String.format("%s装备了%s",
                        DataManager.getCardDesc(pC2.code).name,
                        DataManager.getCardDesc(pC1.code).name));
            }
            case MSG_LPUPDATE -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int val = pktData.getInt();
                mainGame.dInfo.lp[player] = val;
                clientMove.log(String.format("LP = %d", mainGame.dInfo.lp[player]));
            }
            case MSG_UNEQUIP -> {
                int c1 = mainGame.LocalPlayer(pktData.get());
                int l1 = pktData.get();
                int s1 = pktData.get();
                pktData.get();
                ClientCard pC1 = mainGame.dField.getCard(c1, l1, s1);
                pC1.equipTarget.equipped.remove(pC1);
                pC1.equipTarget = null;
                clientMove.log(String.format("%s解除了装备了%s",
                        DataManager.getCardDesc(pC1.equipTarget.code).name,
                        DataManager.getCardDesc(pC1.code).name));
            }
            case MSG_CARD_TARGET -> {
                int c1 = mainGame.LocalPlayer(pktData.get());
                int l1 = pktData.get();
                int s1 = pktData.get();
                pktData.get();
                int c2 = mainGame.LocalPlayer(pktData.get());
                int l2 = pktData.get();
                int s2 = pktData.get();
                pktData.get();
                ClientCard pC1 = mainGame.dField.getCard(c1, l1, s1);
                ClientCard pC2 = mainGame.dField.getCard(c2, l2, s2);
                pC1.cardTarget.add(pC2);
                pC2.ownerTarget.add(pC1);
            }
            case MSG_CANCEL_TARGET -> {
                int c1 = mainGame.LocalPlayer(pktData.get());
                int l1 = pktData.get();
                int s1 = pktData.get();
                pktData.get();
                int c2 = mainGame.LocalPlayer(pktData.get());
                int l2 = pktData.get();
                int s2 = pktData.get();
                pktData.get();
                ClientCard pC1 = mainGame.dField.getCard(c1, l1, s1);
                ClientCard pC2 = mainGame.dField.getCard(c2, l2, s2);
                pC1.cardTarget.remove(pC2);
                pC2.ownerTarget.remove(pC1);
            }
            case MSG_PAY_LPCOST -> {
                int player = mainGame.LocalPlayer(pktData.get());
                int cost = pktData.getInt();
                int finalLp = mainGame.dInfo.lp[player] - cost;
                if (finalLp < 0)
                    finalLp = 0;
                mainGame.dInfo.lp[player] = finalLp;
                clientMove.log(String.format("LP = %d", mainGame.dInfo.lp[player]));
            }
            case MSG_ATTACK -> {
                int ca = mainGame.LocalPlayer(pktData.get());
                int la = pktData.get();
                int sa = pktData.get();
                pktData.get();
                mainGame.dField.attacker = mainGame.dField.getCard(ca, la, sa);
                int cd = mainGame.LocalPlayer(pktData.get());
                int ld = pktData.get();
                int sd = pktData.get();
            }
            case MSG_BATTLE -> {
                // TODO 描述类
            }
            case MSG_ATTACK_DISABLED -> {
                clientMove.log(Convertor.stringFormatterC(
                        DataManager.getSysString(1621),
                        DataManager.getCardDesc(mainGame.dField.attacker.code).name));
            }
            case MSG_DAMAGE_STEP_START, MSG_DAMAGE_STEP_END -> {
            }
            case MSG_MISSED_EFFECT -> {
                pktData.getInt();
                int code = pktData.getInt();
                clientMove.log(Convertor.stringFormatterC(
                        DataManager.getSysString(1622),
                        DataManager.getCardDesc(code).name));
            }
            case MSG_ANNOUNCE_RACE -> {
                pktData.get();
                mainGame.dField.announce_count = pktData.get();
                int available = pktData.getInt();
                ArrayList<SelectOption> opts = new ArrayList<>();
                for (int i = 0, filter = 0x1; i < RACE_COUNT; i++, filter <<= 1) {
                    if ((filter & available) != 0) {
                        opts.add(new SelectOption(DataManager.getSysString(1020 + i), i));
                    }
                }
                int raceAns = 0;
                while (mainGame.dField.announce_count > 0) {
                    SelectOption ans = clientMove.select(opts);
                    raceAns |= (0x1 << ans.value);
                    mainGame.dField.announce_count--;
                }
                setResponseI(raceAns);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_ANNOUNCE_ATTRIB -> {
                pktData.get();
                mainGame.dField.announce_count = pktData.get();
                int available = pktData.getInt();
                ArrayList<SelectOption> opts = new ArrayList<>();
                for (int i = 0, filter = 0x1; i < RACE_COUNT; i++, filter <<= 1) {
                    if ((filter & available) != 0) {
                        opts.add(new SelectOption(DataManager.getSysString(1010 + i), i));
                    }
                }
                int raceAns = 0;
                while (mainGame.dField.announce_count > 0) {
                    SelectOption ans = clientMove.select(opts);
                    raceAns |= (0x1 << ans.value);
                    mainGame.dField.announce_count--;
                }
                setResponseI(raceAns);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case MSG_ANNOUNCE_CARD -> {
                // not native
                pktData.get();
                int count = pktData.get();
                mainGame.dField.declare_opcodes.clear();
                ArrayList<SelectOption> opts = new ArrayList<>();
                int code = 0;
                CardString card;
                for (int i = 0; i < count; i++) {
                    code = pktData.getInt();
                    card = DataManager.getCardDesc(code);
                    if (card != null) {
                        opts.add(new SelectOption(card.name, code));
                    }
                }
                if (select_hint != 0)
                    clientMove.log(DataManager.getDesc(select_hint));
                else
                    clientMove.log(DataManager.getDesc(564));
                select_hint = 0;
                SelectOption ans = clientMove.select(opts);
                setResponseI(ans.value);
                try {
                    sendResponse();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            case MSG_ANNOUNCE_NUMBER -> {
                // TODO
            }
            // case MSG_RELOAD_FIELD ->{
            // mainGame.dInfo.duel_rule = pktData.get();
            // int val = 0;
            // for(int i=0;i<val;i++){
            // int p = mainGame.LocalPlayer(i);
            // mainGame.dInfo.lp[p] = pktData.getInt();
            // }
            // }
        }

        try {
            clientMove.onDuelMsg(curMsg, this);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void setResponseI(int num) {
        try {
            BitWriter.writeInt32(response_buf, num, 0);
            response_len = 4;
        } catch (BitException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setResponseB(byte[] respBuff, int len) {
        for (int i = 0; i < len; i++) {
            response_buf[i] = respBuff[i];
        }
        response_len = len;
    }

    public void sendResponse() throws IOException {
        switch ((mainGame.dInfo.curMsg)) {
            case MSG_SELECT_BATTLECMD, MSG_SELECT_IDLECMD -> {
                mainGame.dField.clearCommandFlag();
            }
            case MSG_SELECT_CARD, MSG_SELECT_COUNTER, MSG_SELECT_SUM, MSG_SELECT_TRIBUTE, MSG_SELECT_UNSELECT_CARD -> {
                mainGame.dField.clearSelect();
            }
            case MSG_SELECT_CHAIN -> {
                mainGame.dField.clearChainSelect();
            }
        }
        mainGame.dInfo.time_player = 2;
        byte[] sendBuf = new byte[response_len];
        for (int i = 0; i < response_len; i++) {
            sendBuf[i] = response_buf[i];
        }
        sendToServer(CTOS_RESPONSE, sendBuf);
    }

}