package life.pisay.transfer.server.core;

import life.pisay.transfer.server.connect.DataConnectionFactory;
import life.pisay.transfer.server.connect.impl.IODataConnectionFactory;

import java.net.Socket;

/**
 * 文件传输 Session
 *
 * @author CAI
 * @time 2021/12/28
 */
public class FileTransferSession {

    /**
     * 数据连接工厂
     */
    private final DataConnectionFactory connectionFactory;

    /**
     * 服务会话
     */
    private final ServerSession serverSession;
    
    public FileTransferSession(ServerSession serverSession, Socket socket) {
    	this.serverSession = serverSession;
        this.connectionFactory = new IODataConnectionFactory(serverSession.getDataTransferGenerator(), socket);
    }

    public DataConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

	public ServerSession getServerSession() {
		return serverSession;
	}
}
