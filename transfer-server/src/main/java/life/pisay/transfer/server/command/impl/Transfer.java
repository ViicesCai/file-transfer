package life.pisay.transfer.server.command.impl;

import life.pisay.transfer.common.utils.FileUtils;
import life.pisay.transfer.common.utils.IOUtils;
import life.pisay.transfer.common.utils.ParameterFormatter;
import life.pisay.transfer.common.utils.StringUtils;
import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.connect.DataConnection;
import life.pisay.transfer.server.connect.DataConnectionFactory;
import life.pisay.transfer.server.connect.FileTransferConnection;
import life.pisay.transfer.server.core.FileTransferSession;
import life.pisay.transfer.server.enums.Reply;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Map;

/**
 * 传输文件
 *
 * @author CAI
 * @time 2021/12/28
 */
public class Transfer implements Command {

    @Override
    public String name() {
        return "transfer".toUpperCase();
    }

    @Override
    public Boolean execute(FileTransferConnection connection, String args) throws IOException {

        if (args == null) {
            connection.send(Reply.REPLY_500, "Syntax error in parameters or arguments.");
            return true;
        }

        FileTransferSession session = connection.getFileTransferSession();
        DataConnectionFactory connectionFactory = session.getConnectionFactory();

        File file;
        long transferSize = 0L;
        try {
            Map<String, String> params = new ParameterFormatter(args).getParams();

            if (!params.containsKey("fileName")) {
            	connection.send(Reply.REPLY_500, "Syntax error in parameters or arguments.");
                return true;
			}

            String fileName = params.get("fileName");

            // 目标目录路径
            String targetDirPath = FileUtils.getDesktopDirPath() + File.separator;

            // 接收中的文件
            File transferringFile = new File(targetDirPath + FileUtils.getSimpleName(fileName) + ".tmp");

            // 接收完的文件
            file = new File(targetDirPath + fileName);

            InetSocketAddress address = connectionFactory.init();
            connection.send(Reply.REPLY_150, 
            		StringUtils.repalce(
            				Reply.REPLY_150.getInfo(), 
            				StringUtils.contact(
            						address.getHostString(), 
            						":", 
            						String.valueOf(address.getPort()))));

            System.out.println("StoragePath: " + file.getAbsolutePath());
            boolean isFailure = false;
            OutputStream out = null;

            DataConnection dataConnection;
            try {
                dataConnection = connectionFactory.open();

            } catch (IOException e) {
                connection.send(Reply.REPLY_500, "Can't open data connection.");
                return true;
            }

            try {
                out = Files.newOutputStream(transferringFile.toPath());
                transferSize = dataConnection.transferFromClient(out);

                out.close();
            } catch (IOException e) {
                isFailure = true;
                connection.send(Reply.REPLY_500, "IOException during data transfer");

            } finally {
                IOUtils.close(out);
            }

            if (!isFailure) {
                boolean rename = transferringFile.renameTo(file);
                if (rename) {
                    System.out.println("接收文件：" + file.getName() + " ,接收大小：" + transferSize);
                }

                connection.send(Reply.REPLY_200, "File transfer over.");

            } else {
                connection.send(Reply.REPLY_500, "File transfer fail.");
            }
        } finally {
            session.getConnectionFactory().close();
        }

        return true;
    }
}
