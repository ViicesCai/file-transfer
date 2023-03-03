package life.pisay.transfer.server.core;

import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.command.CommandFactory;
import life.pisay.transfer.server.connect.FileTransferConnection;
import life.pisay.transfer.server.connect.impl.IOFileTransferConnection;
import life.pisay.transfer.server.enums.Reply;

import java.io.IOException;
import java.net.Socket;

/**
 * 文件传输 Socket
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class FileTransferSocket implements Runnable {

    private final Socket socket;

    private final FileTransferServer server;

    public FileTransferSocket(Socket socket, FileTransferServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            FileTransferConnection connection = new IOFileTransferConnection(server.getServerSession(), socket);
            connection.send(200, "File Transmission Server is ready!");

            String line;

            while ((line = connection.read()) != null) {
                System.out.println("LineData : " + line);

                // 请求解析
                FileTransferRequest request = new FileTransferRequest(line);
                CommandFactory commandFactory = server.getCommandFactory();
                Command command = commandFactory.getCommand(request.getCommand());

                if (command != null) {
                    if (!command.execute(connection, request.getArgs())) {
                        break;
                    }
                } else {
                    connection.send(Reply.REPLY_500, "Syntax error, Unknown Command.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
