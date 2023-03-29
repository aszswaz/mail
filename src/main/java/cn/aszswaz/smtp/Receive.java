package cn.aszswaz.smtp;

import cn.aszswaz.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

/**
 * 接收邮件
 */
@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class Receive implements Runnable {
    private final Socket client;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Receive(Socket client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(
                this.client.getInputStream(), StandardCharsets.UTF_8
        ));
        this.writer = new BufferedWriter(new OutputStreamWriter(
                this.client.getOutputStream(), StandardCharsets.UTF_8
        ));
    }

    public static void start() throws IOException {
        int port = Integer.parseInt(Constant.getProperty("smtp.port"));
        log.info("listen port: {}", port);
        try (ServerSocket socket = new ServerSocket(port)) {
            Executor threadPool = Constant.getThreadPool();
            while (true) {
                Socket client = socket.accept();
                InetAddress address = client.getInetAddress();
                log.info("client IP: {}", address.getHostAddress());
                Receive task = new Receive(client);
                threadPool.execute(task);
            }
        }
    }


    @Override
    public void run() {
        try {
            this.responseCmd(220, Constant.getProperty("smtp.host"));
            // TODO: 解析并执行 SMTP 指令
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                this.client.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void responseCmd(int code, String msg) throws IOException {
        this.writer.write(code + " " + msg + "\r\n");
        this.writer.flush();
    }
}
