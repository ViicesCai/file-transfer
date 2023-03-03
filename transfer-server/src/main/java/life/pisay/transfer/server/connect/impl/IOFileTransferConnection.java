package life.pisay.transfer.server.connect.impl;

import life.pisay.transfer.common.utils.StringUtils;
import life.pisay.transfer.server.connect.FileTransferConnection;
import life.pisay.transfer.server.core.FileTransferSession;
import life.pisay.transfer.server.core.ServerSession;
import life.pisay.transfer.server.enums.Reply;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * IO 连接实现类
 *
 * @author CAI
 * @time 2021/12/27
 */
public class IOFileTransferConnection implements FileTransferConnection {

    private final Socket socket;

    private final FileTransferSession session;

    private final BufferedReader input;

    private final BufferedWriter output;

    public IOFileTransferConnection(ServerSession serverSession, Socket socket) throws IOException {
        this.socket = socket;
        this.session = new FileTransferSession(serverSession, socket);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public FileTransferSession getFileTransferSession() {
        return session;
    }

    @Override
    public void send(int code, String msg) throws IOException {
        output.write(StringUtils.contact(String.valueOf(code), " ", msg, "\r\n"));
        output.flush();
    }
    
    @Override
	public void send(Reply reply) throws IOException {
    	send(reply.getCode(), reply.getInfo());
	}
    
    @Override
   	public void send(Reply reply, String msg) throws IOException {
       	send(reply.getCode(), msg);
   	}

    @Override
    public String read() throws IOException {
        return input.readLine();
    }

    @Override
    public void close() {
        try {
            if (output != null) {
                output.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (input != null) {
                input.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
