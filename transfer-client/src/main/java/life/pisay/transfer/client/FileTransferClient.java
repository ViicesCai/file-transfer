package life.pisay.transfer.client;

import life.pisay.transfer.client.core.ClientMessage;
import life.pisay.transfer.client.core.ClientSocket;
import life.pisay.transfer.common.dto.FileDTO;
import life.pisay.transfer.common.utils.FileUtils;
import life.pisay.transfer.common.utils.IOUtils;
import life.pisay.transfer.common.utils.ParameterFormatter;
import life.pisay.transfer.common.utils.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * 文件传输客户端
 *
 * @author Viices Cai
 * @time 2022/7/20
 */
public class FileTransferClient {

    private final ClientSocket socket;

    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 2 << 12;

    /**
     * 创建文件传输客户端
     *
     * @param receiverHost 接收方主机
     * @throws IOException IO 异常
     */
    public FileTransferClient(String receiverHost) throws IOException {

        ClientSocket client = new ClientSocket(new Socket(receiverHost, 30088));

        ClientMessage message = client.read();
        String code = message.getCode();

        if (!"200".equals(code)) {
            throw new IOException("Init Connection Fail.");
        }

        this.socket = client;
    }

    /**
     * 根据文件路径与文件名获取文件 DTO
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 文件 DTO
     * @throws IOException IO 异常
     */
    public FileDTO seekFileByPath(String filePath, String fileName) throws IOException {
        socket.send("seek filePath:" + filePath + ",fileName:" + fileName);
        ClientMessage message = socket.read();

        if ("200".equals(message.getCode())) {

            return new ParameterFormatter(message.getMessage()).toFileDTO();

        } else if ("400".equals(message.getCode())) {

            throw new FileNotFoundException("文件不存在!");
        }

        return null;
    }

    /**
     * 发送文件，使用文件别称
     *
     * @param file 文件
     * @param fileAlias 文件别名
     * @return 发送结果，true : 成功
     */
    public Boolean sendFile(File file, String fileAlias) throws IOException {

        return onSendFile(file, fileAlias);
    }

    /**
     * 发送文件
     *
     * @param file 文件
     * @return 发送结果，true : 成功
     */
    public Boolean sendFile(File file) throws IOException {

        return onSendFile(file, null);
    }

    /**
     * 发送多文件
     *
     * @param files 文件列表
     * @return 发送结果，true : 成功
     * @throws IOException IO 异常
     */
    public Boolean sendFiles(List<File> files) throws IOException {
        boolean flag;

        for (File file : files) {
            flag = onSendFile(file, null);

            if (!flag) {
                throw new IOException("file : " + file.getName() + " Transfer fail");
            }
        }

        return true;
    }

    /**
     * 恢复发送
     *
     * @param file 文件
     * @param fileDTO 文件传输对象
     * @return 发送结果，true : 成功
     */
    public Boolean restoreSend(File file, FileDTO fileDTO) throws IOException {

        return this.onRestoreSend(file, fileDTO);
    }

    /**
     * 发送文件逻辑方法
     *
     * @param file 文件
     * @param fileAlias 文件别名
     * @return 发送结果
     * @throws IOException IO 异常
     */
    private Boolean onSendFile(File file, String fileAlias) throws IOException {

        if (!file.exists()) {
            throw new FileNotFoundException("Ops! File Not Found.");
        }

        if (fileAlias == null) {
            fileAlias = file.getName();

        } else if (!fileAlias.contains(".")) {
            fileAlias += FileUtils.getFileExtension(file);
        }

        String command = StringUtils.contact("transfer fileName:", fileAlias);

        return this.onTransfer(command, new FileInputStream(file));
    }

    /**
     * 恢复发送逻辑
     *
     * @param file 文件
     * @param fileDTO 文件 DTO
     * @return 恢复发送结果
     * @throws IOException IO 异常
     */
    private Boolean onRestoreSend(File file, FileDTO fileDTO) throws IOException {

        if (!file.exists()) {
            throw new FileNotFoundException("Ops! File Not Found.");
        }

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(fileDTO.getSize());

        String command = StringUtils.contact(
                "restore fileName:",
                fileDTO.getName(),
                ",filePath:",
                fileDTO.getPath(),
                ",currentSize:",
                String.valueOf(fileDTO.getSize()));


        return this.onTransfer(command, new FileInputStream(raf.getFD()) {

            @Override
            public void close() throws IOException {
                super.close();
                raf.close();
            }
        });
    }

    /**
     * 传输方法
     *
     * @param command 请求指令
     * @return 传输结果
     * @throws FileNotFoundException 文件未找到
     */
    private Boolean onTransfer(String command, FileInputStream fis) throws IOException {

        Socket dataSocket = null;
        DataOutputStream out = null;
        BufferedInputStream in = null;

        if (fis == null) {
            throw new IOException("文件输入流为空!");
        }

        try {
            socket.send(command);

            ClientMessage message = socket.read();
            String code = message.getCode();
            String reply = message.getMessage();

            if ("150".equals(code)) {
                int index = reply.indexOf(":");
                String address = reply.substring(index + 1).trim();

                index = address.indexOf(":");
                String host = address.substring(0, index);
                int port = Integer.parseInt(address.substring(index + 1));

                dataSocket = new Socket(host, port);

                in = new BufferedInputStream(fis);
                out = new DataOutputStream(dataSocket.getOutputStream());

                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }

                // 传输完毕，关闭数据通道
                IOUtils.close(dataSocket);

                message = socket.read();
                if ("200".equals(message.getCode())) {
                    return true;
                }
            } else {
                System.out.println("Message : " + message.getRaw());
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                IOUtils.close(dataSocket);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return false;

        } finally {
            try {
                IOUtils.close(out);
                IOUtils.close(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            socket.send("quit");
            ClientMessage message = socket.read();
            if ("221".equals(message.getCode())) {
                System.out.println(message.getRaw());
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        FileTransferClient client = null;

        try {
            client = new FileTransferClient("127.0.0.1");

            Boolean isSuccess = client.sendFile(new File("C:\\Users\\Viices Cai\\Downloads\\你不知道的JavaScript（上中下合集） (作者 [美] Kyle Simpson 译者 赵望野 梁杰 单业 姜南) (Z-Library).pdf"), "kali");

            if (isSuccess) {
                System.out.println("传输成功！");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}