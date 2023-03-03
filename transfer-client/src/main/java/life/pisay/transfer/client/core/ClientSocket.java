package life.pisay.transfer.client.core;

import java.io.*;
import java.net.Socket;

/**
 * 客户端连接
 *
 * @author Viices Cai
 * @time 2022/7/24
 */
public class ClientSocket {

    private final Socket socket;

    private final BufferedReader in;

    private final BufferedWriter out;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * 读取
     *
     * @return 客户端消息
     * @throws IOException IO 异常
     */
    public ClientMessage read() throws IOException {
        return new ClientMessage(in.readLine());
    }

    /**
     * 发送
     *
     * @param msg 消息
     * @throws IOException IO 异常
     */
    public void send(String msg) throws IOException {
        out.write(msg + "\r\n");
        out.flush();
    }

    /**
     * 关闭
     *
     * @throws IOException IO 异常
     */
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
