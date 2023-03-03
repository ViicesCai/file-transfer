package life.pisay.transfer.server;

import life.pisay.transfer.server.core.FileTransferServer;

/**
 * 服务主类
 *
 * @author Viices Cai
 * @time 2023/3/3
 */
public class ServerMain {

    public static void main(String[] args) {

        new Thread(new FileTransferServer()).start();
    }
}
