package life.pisay.transfer.server.connect.impl;

import life.pisay.transfer.common.utils.IOUtils;
import life.pisay.transfer.server.connect.DataConnection;
import life.pisay.transfer.server.connect.DataConnectionFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * IO 数据连接实现类
 *
 * @author CAI
 * @time 2021/12/27
 */
public class IODataConnection implements DataConnection {

    private final Socket socket;

    private final DataConnectionFactory factory;

    public IODataConnection(Socket socket, DataConnectionFactory factory) {
        this.socket = socket;
        this.factory = factory;
    }

    @Override
    public Long transferFromClient(OutputStream out) throws IOException {
        InputStream in = getDataInputStream();

        try {
            return onTransfer(in, out);

        } finally {
            IOUtils.close(in);
        }
    }

    @Override
    public Long transferToClient(InputStream in) throws IOException {
        OutputStream out = getDataOutputStream();

        try {
            return onTransfer(in, out);

        } finally {
            IOUtils.close(out);
        }
    }

    @Override
    public void transferToClient(String str) throws IOException {
        OutputStream out = getDataOutputStream();

        // 字符输入流
        Writer writer = null;

        try {
            writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(str);

        } finally {
            if (writer != null) {
                writer.flush();
            }

            IOUtils.close(writer);
        }
    }

    /**
     * 传输
     *
     * @param in 输入流
     * @param out 输出流
     * @return 传输的字节长度
     * @throws IOException IO 连接异常
     */
    private Long onTransfer(InputStream in, OutputStream out) throws IOException {

        // 传输数据的大小
        long transferSize = 0L;

        // 缓冲区
        byte[] buffer = new byte[1024 * 4];

        BufferedInputStream bis;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(out);

            while (true) {
                int count = bis.read(buffer);

                if (count == -1) {
                    break;
                }

                bos.write(buffer, 0, count);
                transferSize += count;
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (bos != null) {
                bos.flush();
            }
        }

        return transferSize;
    }

    /**
     * 获取数据输入流
     *
     * @return 数据输入流
     * @throws IOException IO 异常
     */
    private InputStream getDataInputStream() throws IOException {
        try {
            Socket dataSocket = socket;

            if (dataSocket == null) {
                throw new IOException("Cannot open data connection.");
            }

            return dataSocket.getInputStream();

        } catch (IOException e) {
            factory.close();
            throw e;
        }
    }

    /**
     * 获取数据输出流
     *
     * @return 数据输出流
     * @throws IOException IO 异常
     */
    private OutputStream getDataOutputStream() throws IOException {
        try {
            Socket dataSocket = socket;

            if (dataSocket == null) {
                throw new IOException("Cannot open data connection.");
            }

            return dataSocket.getOutputStream();

        } catch (IOException e) {
            factory.close();

            throw e;
        }
    }
}
