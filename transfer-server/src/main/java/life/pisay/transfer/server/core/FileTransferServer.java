package life.pisay.transfer.server.core;

import life.pisay.transfer.server.command.CommandFactory;
import life.pisay.transfer.server.command.impl.FileTransferCommandFactoryConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 文件传输服务
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class FileTransferServer implements Runnable {

    private final Executor threadPool = Executors.newCachedThreadPool();

    private ServerSession serverSession;

    private CommandFactory commandFactory;

    @Override
    public void run() {
        System.out.println("<========== Server Start ==========>");

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(30088);
            commandFactory = new FileTransferCommandFactoryConstructor().createCommandFactory();
            serverSession = new ServerSession(new DataTransferGenerator(5000, 5555));

            while (true) {
                final Socket accept = serverSocket.accept();
                threadPool.execute(new FileTransferSocket(accept, this));
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指令工厂
     *
     * @return 指令工厂
     */
    public CommandFactory getCommandFactory() {

        return commandFactory;
    }

    /**
     * 获取服务会话
     *
     * @return 服务会话
     */
    public ServerSession getServerSession() {

        return serverSession;
    }
}
