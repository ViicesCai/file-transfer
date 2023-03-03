package life.pisay.transfer.server.connect;

import java.io.IOException;

import life.pisay.transfer.server.core.FileTransferSession;
import life.pisay.transfer.server.enums.Reply;

/**
 * 文件传输连接接口
 *
 * @author CAI
 * @time 2021/12/27
 */
public interface FileTransferConnection {

    /**
     * 获取文件传输 Session
     *
     * @return 连接的 File
     */
    FileTransferSession getFileTransferSession();

    /**
     * 发送
     *
     * @param code 指令码
     * @param msg 消息
     * @throws IOException 异常
     */
    void send(int code, String msg) throws IOException;

    /**
     * 发送
     * 
     * @param reply 响应枚举
     * @throws IOException 异常
     */
    void send(Reply reply, String msg) throws IOException;
    
    /**
     * 发送
     * 
     * @param reply 响应枚举
     * @throws IOException 异常
     */
    void send(Reply reply) throws IOException;
    
    /**
     * 读取
     *
     * @return 读取信息
     * @throws IOException 异常
     */
    String read() throws IOException;

    /**
     * 连接关闭
     */
    void close();
}
