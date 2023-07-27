package InterfaceImpls;

import com.ocg.Client.ClientCard;
import com.ocg.Moment.Client.ClientInterface;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.Client.SelectOption;
import com.ocg.Moment.InputInterface.InputInterface;
import com.ocg.Moment.OutputInterface.OutputInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class MyClient implements ClientInterface {
    LogicClient logicClient;

    public MyClient(LogicClient client) throws IOException {
        logicClient = client;
        run();
    }

    Scanner scanner;
    boolean signal = false;
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
                return options.get(Integer.valueOf(input));
            }
        }
    }

    synchronized void setInput(String s) {
        this.currentInput = s;
    }

    synchronized String getInput() {
        return this.currentInput;
    }

    void clientAct(String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (input) {
                    case "join" -> {
                        try {
                            logicClient.join233("Moment");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "ready" -> {
                        try {
                            logicClient.ready();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // 查询可进行的操作
                    case "queryAction" -> {
                        StringBuilder actions = new StringBuilder("可进行的操作：\n");
                        if (LogicClient.mainGame.dField.summonable_cards.size() != 0) {
                            actions.append("通常召唤-summon\n");
                        }
                        if (LogicClient.mainGame.dField.msetable_cards.size() != 0) {
                            actions.append("盖放怪兽-setMonster\n");
                        }
                        if (LogicClient.mainGame.dField.ssetable_cards.size() != 0) {
                            actions.append("盖放魔陷-setSpell\n");
                        }
                        if (LogicClient.mainGame.dField.spsummonable_cards.size() != 0) {
                            actions.append("特殊召唤-spSummon\n");
                        }
                        if (LogicClient.mainGame.dField.activatable_cards.size() != 0) {
                            actions.append("发动效果-activate\n");
                        }
                        log(actions.toString());
                    }
                    case "summon" -> {
                        SelectOption ans = select(SelectOption.getOptions(logicClient.mainGame.dField.summonable_cards));
                        LogicClient.setResponseI(ans.value << 16);
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "set" -> {
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
                                SelectOption ansM = select(SelectOption.getOptions(logicClient.mainGame.dField.msetable_cards));
                                LogicClient.setResponseI((ansM.value << 16) + 3);
                            }
                            case 1 -> {
                                SelectOption ansS = select(SelectOption.getOptions(logicClient.mainGame.dField.ssetable_cards));
                                LogicClient.setResponseI((ansS.value << 16) + 4);
                            }
                        }
                        try {
                            logicClient.sendResponse();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "activate" -> {
                        SelectOption ans = select(SelectOption.getOptions(logicClient.mainGame.dField.activatable_cards));
                        LogicClient.setResponseI((ans.value << 16) + 5);
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
