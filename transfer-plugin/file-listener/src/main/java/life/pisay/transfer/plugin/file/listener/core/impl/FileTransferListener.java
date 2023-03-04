package life.pisay.transfer.plugin.file.listener.core.impl;

import life.pisay.transfer.plugin.file.listener.core.FileListener;
import life.pisay.transfer.client.FileTransferClient;
import life.pisay.transfer.common.dto.FileDTO;
import life.pisay.transfer.common.utils.FileUtils;
import life.pisay.transfer.common.utils.NetWorkUtils;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 传输文件监听类
 *
 * @author Viices Cai
 * @time 2022/6/29
 */
public class FileTransferListener implements FileListener {

    private final Map<String, Boolean> threadLockMapper = new ConcurrentHashMap<>();

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public FileTransferListener() { }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

        FileTransferSequence fileSequence = FileTransferSequence.getInstance();
        Path rootPath = Paths.get(fileAlterationObserver.getDirectory().toURI());

        try {
            Files.list(rootPath).forEach(path -> {

                if (path.toFile().isFile()) {
                    return;
                }

                String address = path.toFile().getName();

                if (threadLockMapper.get(address) == null) {
                    threadLockMapper.put(address, false);

                } else if (threadLockMapper.get(address)) {
                    //System.out.println(address + " 线程执行中，跳过！");
                    return;
                }

                try {
                    Files.walk(path).forEach(data -> {

                        if (Files.isRegularFile(data)) {
                            if (".crdownload".equals(FileUtils.getFileExtension(data.toFile()))) {
                                return;
                            }

                            System.out.println(data.toFile().getName());

                            try {
                                Instant lastModified = Files.getLastModifiedTime(data).toInstant().truncatedTo(ChronoUnit.SECONDS);
                                Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);

                                long between = ChronoUnit.SECONDS.between(now, lastModified);
                                if (between >= -2 && between <= 2) {
                                    System.out.println("文件正在复制中，略过...");
                                    return;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            fileSequence.put(address, new TransferDTO(data.toFile()));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectoryCreate(File file) {
    }

    @Override
    public void onDirectoryChange(File file) {

    }

    @Override
    public void onDirectoryDelete(File file) {

    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("添加了 : " + file.getAbsolutePath());
    }

    @Override
    public void onFileChange(File file) {

    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

        FileTransferSequence fileSequence = FileTransferSequence.getInstance();

        Set<String> hosts = fileSequence.keySet();
        hosts.forEach(host -> {

            if (threadLockMapper.get(host) == null) {
                threadLockMapper.put(host,false);

            } else if (threadLockMapper.get(host)) {
                return;
            }

            if (fileSequence.get(host).isEmpty()) {
                return;
            }

            threadPool.execute(() -> {
                try {
                    // 检查网络是否可达
                    if (!NetWorkUtils.checkReachableByICMP(Inet4Address.getByName(host), TimeUnit.SECONDS, 30)) {
                        System.out.println(host + " 不可达!");
                        return;
                    }

                    if (!NetWorkUtils.checkServerIsAvailable(Inet4Address.getByName(host), 30088, TimeUnit.SECONDS, 30)) {
                        System.out.println(host + " 传输服务不可用!");
                        return;

                    } else {
                        System.out.println(host + " 传输服务就绪!");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileTransferClient client = null;

                Set<TransferDTO> transmissions = fileSequence.take(host);
                if (transmissions == null || transmissions.isEmpty()) {
                    return;
                }

                Set<String> failures = new HashSet<>();
                String fileName = null;
                try {
                    threadLockMapper.put(host, true);

                    client = new FileTransferClient(host);
                    Iterator<TransferDTO> iterator = transmissions.iterator();
                    try {
                        while (iterator.hasNext()) {
                            TransferDTO transmission = iterator.next();

                            boolean result = false;

                            File file = transmission.getFile();
                            fileName = file.getName();
                            if (transmission.isFailure()) {
                                String tempName = FileUtils.getSimpleName(file);
                                FileDTO fileDTO = client.seekFileByPath("root", tempName + ".tmp");
                                fileDTO.setName(fileName);
                                result = client.restoreSend(file, fileDTO);

                            } else {
                                result = client.sendFile(file);
                            }

                            if (result) {
                                if (file.delete()) {
                                    System.out.println(file.getName() + " : 删除成功!");
                                    iterator.remove();
                                }
                            } else {
                                failures.add(fileName);
                            }
                        }

                    } catch (IOException e) {
                        failures.add(fileName);
                        e.printStackTrace();

                    }  finally {
                        threadLockMapper.put(host, false);
                    }

                } catch (IOException e) {
                    threadLockMapper.put(host, false);
                    e.printStackTrace();

                } finally {
                    if (client != null) {
                        client.close();
                    }
                }

                if (!failures.isEmpty()) {
                    transmissions.forEach((data) -> {
                        if (failures.contains(data.getFile().getName())) {
                            data.setFailure(true);
                        }
                    });
                }

                fileSequence.put(host, new ArrayList<>(transmissions));
            });
        });
    }
}
