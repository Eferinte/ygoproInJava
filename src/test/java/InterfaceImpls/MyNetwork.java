package InterfaceImpls;

import com.ocg.Moment.Client.LogicClient;
import com.ocg.Moment.Network.NetworkBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyNetwork extends NetworkBase {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public MyNetwork(LogicClient client){
        super(client);
    }

    @Override
    public boolean CTOS(byte[] msg) throws IOException {
        if (socket == null) {
            Connect();
        }
        if (outputStream == null) {
            return false;
        } else {
            outputStream.write(msg);
            outputStream.flush();
        }
        return true;
    }

    private void Connect() {
        // TODO 将此部分作为一个可用实现的Client
        String serverAddress = "s1.ygo233.com";  // 服务器的 IP 地址
        int serverPort = 233;  // 服务器的端口号
        try {
            // 建立 Socket 连接
            socket = new Socket(serverAddress, serverPort);
            // 获取输入\输出流
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            // 创建接收消息的线程
            Thread receiveThread = new Thread(() -> {
                try {
                    System.out.println("Connection successful!");

                    // 持续监听响应消息
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = inputStream.read(buffer);
                        onReceived(buffer, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
