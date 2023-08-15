import InterfaceImpls.MultiThread;
import InterfaceImpls.MyClient;
import InterfaceImpls.MyNetwork;
import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.Moment;
import com.ocg.dataController.DataManager;
import com.ocg.utils.ConstantDict.Dictionary;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        Dictionary.load();
        DataManager dm = new DataManager();
        Moment moment = new Moment(MyClient.class, MyNetwork.class);
        LogicClient client = moment.newClient("Eferinte");
        client.join233("TM0,NS#MOMENT");
        client.ready("旅鸟");
//        MultiThread multiThread = new MultiThread();
//        multiThread.startListener();
//        multiThread.run();
    }
}
