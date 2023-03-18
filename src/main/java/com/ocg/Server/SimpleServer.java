package com.ocg.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class SimpleServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket client = serverSocket.accept();
                OutputStream clientOutStream = client.getOutputStream();
                clientOutStream.write(
                        ("HTTP/1.1 200\n"
                                + "Content-Type: text/html\n"
                                + "\n"
                                + "<h1> Hello, web Framework! </h1>").getBytes()
                );
                clientOutStream.flush();

                clientOutStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 