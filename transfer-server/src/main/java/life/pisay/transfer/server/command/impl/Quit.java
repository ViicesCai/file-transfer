package life.pisay.transfer.server.command.impl;

import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.connect.FileTransferConnection;
import life.pisay.transfer.server.enums.Reply;

import java.io.IOException;

/**
 * 退出
 *
 * @author CAI
 * @time 2021/12/28
 */
public class Quit implements Command {

    @Override
    public String name() {
        return "quit".toUpperCase();
    }

    @Override
    public Boolean execute(FileTransferConnection connection, String args) throws IOException {

        connection.send(Reply.REPLY_221, "Thank you for using it, Good bye.");
        connection.getFileTransferSession().getConnectionFactory().close();

        return false;
    }
}
