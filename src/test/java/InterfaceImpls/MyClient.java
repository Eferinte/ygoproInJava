package InterfaceImpls;

import com.ocg.Client.ClientCard;
import com.ocg.Moment.Client.ClientInterface;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.Client.SelectOption;
import com.ocg.Moment.InputInterface.InputInterface;
import com.ocg.Moment.OutputInterface.OutputInterface;
import com.ocg.utils.ConstantDict.Dictionary;
import com.ocg.utils.ConstantDict.EnumStruct;
import com.ocg.utils.ConstantDict.LOCATION;
import com.ocg.utils.Convertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ocg.Constants.*;
import static com.ocg.utils.ConstantDict.Dictionary.locationMap;

public class MyClient implements ClientInterface {
    LogicClient logicClient;

    public MyClient(LogicClient client) throws IOException {
        logicClient = client;
        run();
    }

    Scanner scanner;
    boolean signal = false;
    boolean selecting = false;
    String currentInput = null;

    @Override
    public boolean confirm(String hint) {
        return false;
    }

    @Override
    public int selectCard(ClientCard[] cards) {
        return 0;
    }

    @Override
    public int selectPos(byte position) {
        return 0;
    }

    @Override
    public SelectOption select(ArrayList<SelectOption> options) {
        setIsSelecting(true);
        StringBuilder s = new StringBuilder("请选择：\n");
        for (int i = 0; i < options.size(); i++) {
            s.append((i) + "-" + options.get(i) + ";\n");
        }
        System.out.println(s);
        Pattern pattern = Pattern.compile("^.*[0-9].*$");
        while (true) {
            signal = true;
            String input = getInput();
            if (input != null && pattern.matcher(input).find()) {
                setInput(null);
                setIsSelecting(false);
                return options.get(Integer.valueOf(input));
            }
        }
    }

    synchronized void setInput(String s) {
        this.currentInput = s;
    }

    synchronized void setIsSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    synchronized boolean getIsSelecting() {
        return this.selecting;
    }


    synchronized String getInput() {
        return this.currentInput;
    }

    void clientAct(String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSelecting = false;
                if (getIsSelecting()) isSelecting = true;
                switch (input) {
                    case "join" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        try {
                            logicClient.join233("TM0#MOMENT");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "ready" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        try {
                            logicClient.ready();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // 查询可进行的操作
                    case "queryAct" -> { //TODO 不展示当前阶段不允许的操作
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        StringBuilder actions = new StringBuilder("可进行的操作：\n");
                        if (LogicClient.mainGame.dField.summonable_cards.size() != 0) {
                            actions.append("通常召唤-summon\n");
                        }
                        if (LogicClient.mainGame.dField.msetable_cards.size() != 0) {
                            actions.append("盖放怪兽-set\n");
                        }
                        if (LogicClient.mainGame.dField.ssetable_cards.size() != 0) {
                            actions.append("盖放魔陷-set\n");
                        }
                        if (LogicClient.mainGame.dField.spsummonable_cards.size() != 0) {
                            actions.append("特殊召唤-spSummon\n");
                        }
                        if (LogicClient.mainGame.dField.activatable_cards.size() != 0) {
                            actions.append("发动效果-act\n");
                        }
                        actions.append("阶段转换-go");
                        log(actions.toString());
                    }
                    case "go" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        StringBuilder phase = new StringBuilder("请选择要进行的阶段：\n");
                        ArrayList<SelectOption> opts = new ArrayList<>();
                        if (LogicClient.mainGame.btnBP) {
                            phase.append("进战阶-bp\n");
                            opts.add(new SelectOption("bp", 0));
                        }
                        if (LogicClient.mainGame.btnM2) {
                            phase.append("进入主要阶段2-M2\n");
                            opts.add(new SelectOption("m2", 1));
                        }
                        if (LogicClient.mainGame.btnEP) {
                            phase.append("进入结束阶段-ep\n");
                            opts.add(new SelectOption("ep", 2));
                        }
                        log(phase.toString());
                        SelectOption ans = select(opts);
                        switch (ans.value) {
                            case 0 -> {
                                if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_IDLECMD)
                                    LogicClient.setResponseI(6);
                            }
                            case 1 -> {
                                if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_BATTLECMD)
                                    LogicClient.setResponseI(2);
                            }
                            case 2 -> {
                                if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_BATTLECMD)
                                    LogicClient.setResponseI(3);
                                else if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_IDLECMD)
                                    LogicClient.setResponseI(7);
                            }
                        }
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    case "queryList" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        ArrayList<SelectOption> opts = new ArrayList<>();
                        opts.add(new SelectOption("己方", 0));
                        opts.add(new SelectOption("对方", 1));
                        SelectOption player = select(opts);
                        opts.clear();
                        opts.add(new SelectOption("手卡", 0));
                        opts.add(new SelectOption("怪兽区", 1));
                        opts.add(new SelectOption("魔陷区", 2));
                        opts.add(new SelectOption("墓地", 3));
                        opts.add(new SelectOption("额外", 4));
                        opts.add(new SelectOption("除外区", 5));
                        SelectOption location = select(opts);
                        switch (location.value) {
                            case 0 -> log(LogicClient.mainGame.dField.hand[player.value].toString());
                            case 1 -> log(LogicClient.mainGame.dField.mzone[player.value].toString());
                            case 2 -> log(LogicClient.mainGame.dField.szone[player.value].toString());
                            case 3 -> log(LogicClient.mainGame.dField.grave[player.value].toString());
                            case 4 -> log(LogicClient.mainGame.dField.extra[player.value].toString());
                            case 5 -> log(LogicClient.mainGame.dField.remove[player.value].toString());
                            default -> log("无效输入");
                        }
                    }
                    case "summon" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.summonable_cards.size()==0) {
                            log("当前没有可以召唤的怪兽");
                            return;
                        }
                        SelectOption ans = select(SelectOption.getOptions(LogicClient.mainGame.dField.summonable_cards));
                        LogicClient.setResponseI(ans.value << 16);
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "atk" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.attackable_cards.size()==0) {
                            log("当前没有可以攻击的怪兽");
                            return;
                        }
                        SelectOption ans = select(SelectOption.getOptions(LogicClient.mainGame.dField.attackable_cards));
                        LogicClient.setResponseI((ans.value << 16) + 1);
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "set" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.ssetable_cards.size()==0 && LogicClient.mainGame.dField.msetable_cards.size()==0 ) {
                            log("当前没有可以放置的卡片");
                            return;
                        }
                        ArrayList<SelectOption> opts = new ArrayList();
                        if (LogicClient.mainGame.dField.msetable_cards.size() != 0) {
                            opts.add(new SelectOption("盖放怪兽", 0));
                        }
                        if (LogicClient.mainGame.dField.ssetable_cards.size() != 0) {
                            opts.add(new SelectOption("盖放魔陷", 1));
                        }
                        if (opts.size() == 0) {
                            log("没有可以盖放的卡");
                            break;
                        }
                        SelectOption ans = select(opts);
                        switch (ans.value) {
                            case 0 -> {
                                SelectOption ansM = select(SelectOption.getOptions(LogicClient.mainGame.dField.msetable_cards));
                                LogicClient.setResponseI((ansM.value << 16) + 3);
                            }
                            case 1 -> {
                                SelectOption ansS = select(SelectOption.getOptions(LogicClient.mainGame.dField.ssetable_cards));
                                LogicClient.setResponseI((ansS.value << 16) + 4);
                            }
                        }
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "act" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.activatable_cards.size()==0) {
                            log("当前没有可以发动的效果");
                            return;
                        }
                        SelectOption ans = select(SelectOption.getOptions(LogicClient.mainGame.dField.activatable_cards));
                        if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_IDLECMD)
                            LogicClient.setResponseI((ans.value << 16) + 5);
                        else if (LogicClient.mainGame.dInfo.curMsg == MSG_SELECT_BATTLECMD)
                            LogicClient.setResponseI(ans.value << 16);
                        else
                            LogicClient.setResponseI(ans.value);
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "spSummon" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.spsummonable_cards.size()==0) {
                            log("当前没有可以特殊召唤的怪兽");
                            return;
                        }
                        // TODO 抽取各list
                        log(Convertor.getStringList(LogicClient.mainGame.dField.spsummonable_cards).toString());
                        ArrayList<ClientCard> list = LogicClient.mainGame.dField.spsummonable_cards;
                        ArrayList<SelectOption> options = new ArrayList<>();
                        Map<Integer, Boolean> locationMap = new HashMap<>();
                        ArrayList<SelectOption> handOptions = new ArrayList<>();
                        ArrayList<SelectOption> exOptions = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            ClientCard card = list.get(i);
                            if (card.location == Dictionary.locationMap.get(LOCATION.HAND).value) {
                                handOptions.add(new SelectOption(card.toString(), i));
                            }
                            if (card.location == Dictionary.locationMap.get(LOCATION.EXTRA).value) {
                                exOptions.add(new SelectOption(card.toString(), i));
                            }
                            if (locationMap.get(card.location) != null) continue;
                            for (LOCATION location : Dictionary.locationMap.keySet()) {
                                EnumStruct enumStruct = Dictionary.locationMap.get(location);
                                if (enumStruct.value == card.location) {
                                    locationMap.put(card.location, true);
                                    options.add(new SelectOption(enumStruct.desc, enumStruct.value));
                                }
                            }
                        }
                        SelectOption ans = select(options);
                        switch (ans.value) {
                            case LOCATION_HAND -> {
                                SelectOption target = select(handOptions);
                                LogicClient.setResponseI((target.value << 16) + 1);
                                try {
                                    logicClient.sendResponse();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            case LOCATION_EXTRA -> {
                                SelectOption target = select(exOptions);
                                LogicClient.setResponseI((target.value << 16) + 1);
                                try {
                                    logicClient.sendResponse();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    case "rePos" -> {
                        if (isSelecting) {
                            log("请先完成当前操作！");
                            return;
                        }
                        if(LogicClient.mainGame.dField.reposable_cards.size()==0) {
                            log("当前没有可以修改状态的怪兽");
                            return;
                        }
                        SelectOption ans = select(SelectOption.getOptions(
                                LogicClient.mainGame.dField.reposable_cards
                        ));
                        LogicClient.setResponseI((ans.value << 16) + 2);
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }).start();
    }

    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanner = new Scanner(System.in);
                while (true) {
                    System.out.print("Enter your input (or 'quit' to exit): \n");
                    String input = scanner.nextLine();
                    if (signal == true) {
                        setInput(input);
                        signal = false;
                    }
                    if (input.equalsIgnoreCase("quit")) {
                        System.exit(0);
                    }
                    clientAct(input);
                }
            }
        }).start();
    }

    @Override
    public void log(String operate) {
        System.out.println("[LOG]" + operate);
    }
}
