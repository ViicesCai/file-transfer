package life.pisay.transfer.server.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据传输接口
 *
 * @author CAI
 * @time 2021/12/27
 */
public interface DataConnection {

    /**
     * 从客户端接收数据
     *
     * @param out 输出流
     * @return 传输数据的长度
     * @throws IOException IO 异常
     */
    Long transferFromClient(OutputStream out) throws IOException;

    /**
     * 将数据传输到客户端
     *
     * @param in 输入流
     * @return 传输数据的长度
     * @throws IOException IO异常
     */
    Long transferToClient(InputStream in) throws IOException;

    /**
     * 传输字符串到客户端
     *
     * @param str 字符串
     * @throws IOException IO异常
     */
    void transferToClient(String str) throws IOException;
}
