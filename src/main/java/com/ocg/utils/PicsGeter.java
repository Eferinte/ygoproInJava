package com.ocg.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PicsGeter {
    private static final String BASE_URL = "https://images.ygoprodeck.com/images/cards_cropped/";
    private static final int NUM_THREADS = 10; // 线程数
    private static final int INTERVAL = 1000; // 请求间隔，单位毫秒
    private static final String SAVE_PATH = "./assets/pics/card_images/"; // 保存路径
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Callable<Void>> tasks = new ArrayList<>();
        // 创建File对象
        File dir = new File("./assets/pics");
        // 获取该目录下的所有文件名
        File[] fileNames = dir.listFiles();
        // 遍历文件名数组
        for (File file : fileNames) {
            if(file.isFile()){
                System.out.println(file.getName());
                String imageId = file.getName();
                tasks.add(() -> {
                    try {
                        String imageUrl = BASE_URL + imageId;
                        URL url = new URL(imageUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);

                        int responseCode = conn.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = conn.getInputStream();
                            OutputStream outputStream = new FileOutputStream(SAVE_PATH + imageId);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.close();
                            inputStream.close();
                            System.out.println("Downloaded image " + imageId);
                        } else {
                            System.out.println("Failed to download image " + imageId + ", response code: " + responseCode);
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to download image " + imageId + ", " + e.getMessage());
                    }
                    return null;
                });
            }
        }
        try {
            List<Future<Void>> futures = executor.invokeAll(tasks);
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
