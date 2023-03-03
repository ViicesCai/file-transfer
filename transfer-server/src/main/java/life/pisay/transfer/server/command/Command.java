package life.pisay.transfer.server.command;

import life.pisay.transfer.server.connect.FileTransferConnection;

import java.io.IOException;

/**
 * 指令接口
 *
 * @author CAI
 * @time 2021/12/27
 */
public interface Command {

    /**
     * 指令名称
     *
     * @return 指令名称
     */
    String name();

    /**
     * 执行方法
     *
     * @param connection 文件传输连接
     * @param args 参数
     * @return 执行结果
     * @throws IOException 异常
     */
    Boolean execute(FileTransferConnection connection, String args) throws IOException;
}
