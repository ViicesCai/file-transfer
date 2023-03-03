package life.pisay.transfer.server.connect;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 数据连接工厂
 *
 * @author CAI
 * @time 2021/12/27
 */
public interface DataConnectionFactory {

    /**
     * 初始化数据连接
     *
     * @return 数据连接
     */
    InetSocketAddress init();

    /**
     * 打开数据连接
     *
     * @return 开放的数据连接
     */
    DataConnection open() throws IOException;

    /**
     * 关闭数据连接
     */
    void close();
}
