package life.pisay.transfer.plugin.file.listener;

import life.pisay.transfer.plugin.file.listener.core.FileMonitor;
import life.pisay.transfer.plugin.file.listener.core.impl.FileTransferListener;
import life.pisay.transfer.common.utils.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 监听服务主程
 *
 * @author Viices Cai
 * @time 2023/3/4
 */
public class ListenerMain {

    public static void main(String[] args) throws Exception {

        String path = args[0];
        if (StringUtils.isNull(path)) {
            throw new NullPointerException("需要配置文件 listener.properties 的路径");
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new NullPointerException("配置文件不存在!");
        }

        Map<String, String> map = loadProperties(file);

        FileMonitor monitor = new FileMonitor(new File(map.get("dirPath")), TimeUnit.SECONDS, 1, new FileTransferListener());
        monitor.start();
    }

    /**
     * 读取配置文件
     *
     * @param file 文件对象
     * @return 配置映射
     */
    private static Map<String, String> loadProperties(File file) {

        Properties properties = new Properties();
        Map<String, String> map = new HashMap<>();
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            properties.load(in);

            String dirPath = properties.getProperty("dirPath");

            if (StringUtils.isNull(dirPath)) {
                throw new NullPointerException("缺少必要参数！");
            }

            map.put("dirPath", dirPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
