package life.pisay.transfer.plugin.file.listener.core.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输文件序列
 *
 * @author Viices Cai
 * @time 2022/6/23
 */
public class FileTransferSequence {

    private volatile static FileTransferSequence instance;

    /**
     * 传输映射集
     */
    private final Map<String, Set<TransferDTO>> transmissionMapper;

    /**
     * 获取实例
     *
     * @return TransmissionFileSequence 实例对象
     */
    public static FileTransferSequence getInstance() {

        if (instance == null) {

            synchronized (FileTransferSequence.class) {

                if (instance == null) {
                    instance = new FileTransferSequence();
                }
            }
        }

        return instance;
    }

    /**
     * 单传输对象加入序列
     *
     * @param host 主机地址
     * @param transmissionDTO 传输 DTO
     */
    public void put(String host, TransferDTO transmissionDTO) {

        Set<TransferDTO> transmissionDTOSet = this.loadSetByHost(host);
        transmissionDTOSet.add(transmissionDTO);

        transmissionMapper.put(host, transmissionDTOSet);
    }

    /**
     * 多传输对象加入序列
     *
     * @param host 主机地址
     * @param transmissions 传输对象列表
     */
    public void put(String host, List<TransferDTO> transmissions) {

        Set<TransferDTO> transmissionDTOSet = this.loadSetByHost(host);
        transmissionDTOSet.addAll(transmissions);

        transmissionMapper.put(host, transmissionDTOSet);
    }

    /**
     * 取出主机对应的传输序列：被获取的值将会被清空
     *
     * @param host 主机地址
     * @return 待传输的序列
     */
    public Set<TransferDTO> take(String host) {

        Set<TransferDTO> files = transmissionMapper.get(host);
        transmissionMapper.replace(host, new HashSet<>());

        return files;
    }

    /**
     * 获取传输序列
     *
     * @param host 主机地址
     * @return 待传输的序列
     */
    public Set<TransferDTO> get(String host) {

        return transmissionMapper.get(host);
    }

    /**
     * 获取序列的键集合
     *
     * @return 键集合
     */
    public Set<String> keySet() {

        return transmissionMapper.keySet();
    }

    /**
     * 打印传输序列
     */
    public void print() {

        transmissionMapper.keySet().forEach(data -> System.out.println(data + " : " + transmissionMapper.get(data)));
    }

    /**
     * 根据主机获取对应的传输序列
     *
     * @param host 主机地址
     * @return 文件对象序列
     */
    private Set<TransferDTO> loadSetByHost(String host) {

        return transmissionMapper.containsKey(host) ? transmissionMapper.get(host) : new HashSet<>();
    }

    private FileTransferSequence() {
        this.transmissionMapper = new ConcurrentHashMap<>();
    }
}
