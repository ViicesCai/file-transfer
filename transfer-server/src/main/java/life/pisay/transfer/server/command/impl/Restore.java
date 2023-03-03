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
import java.util.Map;

/**
 * 恢复文件
 *
 * @author Viices Cai
 * @time 2022/6/29
 */
public class Restore implements Command {

    @Override
    public String name() {
        return "restore".toUpperCase();
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

			if (!params.containsKey("fileName") || !params.containsKey("filePath") || !params.containsKey("currentSize")) {
            	connection.send(Reply.REPLY_500, "Syntax error in parameters or arguments.");
                return true;
			}

			String path = params.get("filePath") + File.separator;
			String fileName = params.get("fileName");

			File transferringFile = new File(path + FileUtils.getSimpleName(fileName) + ".tmp");
			file = new File(path + fileName);

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
                long offset = Long.parseLong(params.get("currentSize"));

                RandomAccessFile raf = new RandomAccessFile(transferringFile, "rw");
                raf.seek(offset);

                out = new FileOutputStream(raf.getFD()) {

                    @Override
                    public void close() throws IOException {
                        super.close();
                        raf.close();
                    }
                };
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

                connection.send(Reply.REPLY_200, "File Transfer over.");

            } else {
                connection.send(Reply.REPLY_500, "File Transfer fail.");
            }
		} catch (Exception e) {
			session.getConnectionFactory().close();
		}

        return true;
    }
}
