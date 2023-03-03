package life.pisay.transfer.common.utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class FileUtils {

    /**
     * 获取文件后缀(包含 .)
     *
     * @param file 文件
     * @return 文件后缀
     */
    public static String getFileExtension(File file) {

        if (file == null) {

            return null;
        }

        return getFileExtension(file.getName());
    }

    /**
     * 获取文件后缀(包含 .)
     *
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileExtension(String fileName) {

        if (StringUtils.isNull(fileName)) {

            return null;
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 获取文件名简称(不包含后缀的文件名)
     *
     * @param fileName 文件名
     * @return 文件名简称
     */
    public static String getSimpleName(String fileName) {

        if (StringUtils.isNull(fileName)) {

            return null;
        }

        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取文件名简称(不包含后缀的文件名)
     *
     * @param file 文件
     * @return 文件名简称
     */
    public static String getSimpleName(File file) {

        if (file == null) {

            return null;
        }

        return getSimpleName(file.getName());
    }

    /**
     * 获取当前系统的桌面目录路径
     *
     * @return 桌面目录路径
     */
    public static String getDesktopDirPath() {

        String os = System.getProperties().getProperty("os.name");

        if ("LINUX".equalsIgnoreCase(os)) {
            String homeDirPath = System.getProperties().getProperty("user.home") + File.separator;

            File desktop = new File(homeDirPath + "Desktop");
            if (desktop.isDirectory() && desktop.exists()) {

                return desktop.getAbsolutePath();
            }

            desktop = new File(homeDirPath + "desktop");
            if (desktop.isDirectory() && desktop.exists()) {

                return desktop.getAbsolutePath();
            }

            desktop = new File(homeDirPath + "桌面");
            if (desktop.isDirectory() && desktop.exists()) {

                return desktop.getAbsolutePath();
            }

            if ("/root/".equals(homeDirPath)) {

                return "/home";
            }

        } else {
            File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
            if (!homeDirectory.exists()) {

                return null;
            }

            return homeDirectory.getAbsolutePath();
        }

        return null;
    }

    /**
     * 获取文件的真实大小
     *
     * @param file 文件
     * @return 文件大小
     * @throws FileNotFoundException 文件未找到
     */
    public static Long getFileRealSize(File file) throws FileNotFoundException {

        if (file == null || !file.exists()) {

            throw  new FileNotFoundException("文件不存在!");
        }

        if (file.isDirectory()) {

            throw new IllegalArgumentException("不能为目录!");
        }

        try (FileChannel channel = new FileInputStream(file).getChannel()) {
            return channel.size();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    private FileUtils() { }
}
