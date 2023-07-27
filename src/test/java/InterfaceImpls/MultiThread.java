package InterfaceImpls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MultiThread {
    public String msg;

    public synchronized void setMsg(String msg) {
        this.msg = msg;
    }

    public synchronized String getMsg(){
        return msg;
    }

    public void startListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("begin listening ...");
                while (true) {
                    String msg = getMsg();
                    if (msg!=null) {
                        fileLog();
                        setMsg(null);
                    }
                }
            }
        }).start();
    }

    public void fileLog(){
        String fileName = "log.txt"; // 文件名
        try {
            File file = new File(fileName); // 创建文件对象
            FileWriter writer = new FileWriter(file); // 创建文件写入器

            writer.write("log-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 写入内容
            writer.close(); // 关闭写入器

            System.out.println("File created and written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Scanner scanner = new Scanner(System.in);
                        while (true) {
                            System.out.print("Enter your input (or 'quit' to exit): ");
                            String input = scanner.nextLine();
                            if (input.equals("hello")) {
                                setMsg(input);
                                System.out.println("write success");
                            } else {
                                System.out.println("access denied");
                            }
                        }
                    }
                }
        ).start();
    }
}
