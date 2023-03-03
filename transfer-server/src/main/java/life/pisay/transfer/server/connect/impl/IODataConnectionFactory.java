package life.pisay.transfer.server.connect.impl;

import life.pisay.transfer.server.connect.DataConnection;
import life.pisay.transfer.server.connect.DataConnectionFactory;
import life.pisay.transfer.server.core.DataTransferGenerator;
import life.pisay.transfer.server.exception.DataConnectionException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * IO 数据连接工厂实现类
 *
 * @author CAI
 * @time 2021/12/27
 */
public class IODataConnectionFactory implements DataConnectionFactory {

    /**
     * 数据传输构造器
     */
    private final DataTransferGenerator generator;

    /**
     * IP 地址
     */
    private final InetAddress address;

    /**
     * 数据连接监听
     */
    private ServerSocket serverSocket;

    /**
     * 数据连接端口
     */
    private Integer port;

    /**
     * 数据连接
     */
    private Socket dataSocket;

    public IODataConnectionFactory(DataTransferGenerator generator, Socket socket) {
        this.generator = generator;
        this.address = socket.getLocalAddress();
    }

    @Override
    public InetSocketAddress init() {
        try {
            this.port = generator.obtain();
            serverSocket = new ServerSocket(port, 0, address);

        } catch (IOException | DataConnectionException e) {
            serverSocket = null;
            close();
            e.printStackTrace();
        }

        return new InetSocketAddress(address.getHostAddress(), port);
    }


    @Override
    public DataConnection open() throws IOException {
        dataSocket = null;

        try {
            dataSocket = serverSocket.accept();

        } catch (IOException e) {
            close();
            e.printStackTrace();
        }

        return new IODataConnection(dataSocket, this);
    }

    @Override
    public void close() {
        if (dataSocket != null) {
            try {
                dataSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            dataSocket = null;
        }

        if (serverSocket != null) {
            try {
                serverSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            serverSocket = null;
        }

        if (port != null) {
            if (!generator.release(port)) {
                System.out.println("Data Connection Not Release");
            }
        }
    }
}
