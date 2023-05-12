package com.ocg.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PicServer {
    public static void main(String args[]) throws IOException {
        // 创建HttpServer实例，绑定端口为8906
        HttpServer server = HttpServer.create(new InetSocketAddress(8906), 10);
        // 创建Context，指定处理器
        server.createContext("/image", new ImageHandler());
        server.createContext("/card", new CardHandler());
        // 启动服务器
        server.start();
        System.out.println("Image server started on port 8906.");
    }
    public static class CardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 获取请求的URI
            System.out.println("接收请求");
            String uri = exchange.getRequestURI().toString();
            // 获取项目目录下图片的绝对路径
            String imagePath = "./assets/pics/" + uri.substring("/card/".length()) + ".jpg";
            System.out.println(imagePath);
            File imageFile = new File(imagePath);
            System.out.println("new File");
            // 如果图片文件存在，且是文件且可读，则返回图片
            if (imageFile.exists() && imageFile.isFile() && imageFile.canRead()) {
                // 读取图片文件的内容
                byte[] imageData = Files.readAllBytes(Paths.get(imagePath));
                // 设置响应的 Content-Type 为图片格式
                exchange.getResponseHeaders().set("Content-Type", "image/jpeg");

                // 设置响应的 Content-Length 为图片文件的大小
                exchange.getResponseHeaders().set("Content-Length", String.valueOf(imageData.length));
                // 开发
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.getResponseHeaders().add("Access-Control-Max-Age", "86400"); // 一天内不再检查预请求

                // 发送图片文件的内容作为响应
                exchange.sendResponseHeaders(200, imageData.length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(imageData);
                outputStream.close();
            } else {
                // 如果图片文件不存在，则返回404
                String response = "Image not found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        }
    }
    public static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 获取请求的URI
            System.out.println("接收请求");
            String uri = exchange.getRequestURI().toString();
            // 获取项目目录下图片的绝对路径
            String imagePath = "./assets/pics/card_images/" + uri.substring("/image/".length()) + ".jpg";
            System.out.println(imagePath);
            File imageFile = new File(imagePath);
            System.out.println("new File");
            // 如果图片文件存在，且是文件且可读，则返回图片
            if (imageFile.exists() && imageFile.isFile() && imageFile.canRead()) {
                // 读取图片文件的内容
                byte[] imageData = Files.readAllBytes(Paths.get(imagePath));
                // 设置响应的 Content-Type 为图片格式
                exchange.getResponseHeaders().set("Content-Type", "image/jpeg");

                // 设置响应的 Content-Length 为图片文件的大小
                exchange.getResponseHeaders().set("Content-Length", String.valueOf(imageData.length));
                // 开发
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
                exchange.getResponseHeaders().add("Access-Control-Max-Age", "86400"); // 一天内不再检查预请求

                // 发送图片文件的内容作为响应
                exchange.sendResponseHeaders(200, imageData.length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(imageData);
                outputStream.close();
            } else {
                // 如果图片文件不存在，则返回404
                String response = "Image not found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        }
    }
}
