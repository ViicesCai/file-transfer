package life.pisay.transfer.server.core;

import life.pisay.transfer.server.exception.DataConnectionException;

import java.util.*;

/**
 * 数据传输构造器
 *
 * @author CAI
 * @time 2021/11/26
 */
public class DataTransferGenerator {

    /**
     * 起始范围
     */
    private final Integer begin;

    /**
     * 结束范围
     */
    private final Integer end;

    /**
     * 端口队列
     */
    private Queue<Integer> portQueue;

    public DataTransferGenerator() {
        begin = 3000;
        end = 6000;

        init();
    }

    public DataTransferGenerator(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = begin; i <= end; i++) {
            list.add(i);
        }

        Collections.shuffle(list);

        Integer[] nums = list.toArray(new Integer[0]);

        portQueue = new LinkedList<>(Arrays.asList(nums));
    }

    /**
     * 获取端口
     *
     * @return 端口号
     * @throws DataConnectionException 端口序列为空异常
     */
    public synchronized Integer obtain() throws DataConnectionException {
        if (portQueue.isEmpty()) {

            throw new DataConnectionException("No port is currently available");
        }

        return portQueue.poll();
    }

    /**
     * 释放端口
     *
     * @param port 端口号
     * @return 释放结果
     */
    public synchronized Boolean release(Integer port) {
        if (port == null) {

            throw new NullPointerException("Not release port currently available");
        }

        return portQueue.offer(port);
    }
}
