import com.google.gson.Gson;
import com.ocg.Client.ClientCard;
import org.java_websocket.WebSocket;

import java.util.Vector;

public class test {
    public class Test {
        public String name;
        public WebSocket conn;
        public Test(String name,WebSocket conn){
            this.name = name;
            this.conn = conn;
        }
    }
    public static void main(String[] args) {
        Gson JSON = new Gson();
    }
}
