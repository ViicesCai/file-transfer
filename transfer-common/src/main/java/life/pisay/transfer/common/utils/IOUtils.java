package life.pisay.transfer.common.utils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * IO 工具类
 *
 * @author CAI
 * @time 2021/12/27
 */
public class IOUtils {

    /**
     * 关闭
     *
     * @param in 输出流
     * @throws IOException IO 异常
     */
    public static void close(InputStream in) throws IOException {
        if (in != null) {
            in.close();
        }
    }

    /**
     * 关闭
     *
     * @param out 输入流
     * @throws IOException IO 异常
     */
    public static void close(OutputStream out) throws IOException {
        if (out != null) {
            out.close();
        }
    }

    /**
     * 关闭
     *
     * @param reader 字符输出流
     * @throws IOException IO 异常
     */
    public static void close(Reader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * 关闭
     *
     * @param writer 字符输出流
     * @throws IOException IO 异常
     */
    public static void close(Writer writer) throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    /**
     * 关闭
     * 
     * @param socket 套接字
     * @throws IOException IO 异常
     */
    public static void close(Socket socket) throws IOException {
		if (socket != null) {
			socket.close();
		}
	}
    
    private IOUtils() { }
}
