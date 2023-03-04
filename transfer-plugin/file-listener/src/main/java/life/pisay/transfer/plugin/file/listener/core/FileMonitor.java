package life.pisay.transfer.plugin.file.listener.core;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 文件监控类
 *
 * @author Viices Cai
 * @time 2022/6/23
 */
public class FileMonitor {

    /**
     * 监控执行对象
     */
    private final FileAlterationMonitor monitor;

    /**
     * 文件监控构造方法
     *
     * @param dir 监控目录
     * @param timeUnit 时间单位
     * @param interval 监控间隔
     * @param listener 文件监听接口
     */
    public FileMonitor(File dir, TimeUnit timeUnit, Integer interval, FileListener listener) {
        this.monitor = new FileAlterationMonitor(timeUnit.toMillis(interval));

        FileAlterationObserver observer = new FileAlterationObserver(dir);
        this.monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void start() throws Exception {

        System.out.println("<========== FileListener Start ==========>");

        monitor.start();
    }

    public void stop() throws Exception {
        System.out.println("<========== FileListener Stop ==========>");

        monitor.stop();
    }
}
