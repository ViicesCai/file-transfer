package life.pisay.transfer.common.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * 网络工具集
 *
 * @author Viices Cai
 * @time 2022/4/14
 */
public class NetWorkUtils {

    public static final String DOT = ".";

    /**
     * 获取 IP 地址的网络号
     *
     * @param ipAddress IP 地址
     * @return IP 地址的网络号
     */
    public static String getNetWorkIdByIpAddress(String ipAddress) {

        if (!StringUtils.isNull(ipAddress) && ipAddress.contains(DOT)) {

            return getSimpleNetWorkByIpAddress(ipAddress) + DOT + "0";
        }

        return null;
    }

    /**
     * 获取 IP 地址的简短网络号(xxx.xxx.xxx)
     *
     * @param ipAddress IP 地址
     * @return IP 地址的简短网络号
     */
    public static String getSimpleNetWorkByIpAddress(String ipAddress) {

        if (!StringUtils.isNull(ipAddress) && ipAddress.contains(DOT)) {

            return ipAddress.substring(0, ipAddress.lastIndexOf(DOT));
        }

        return null;
    }

    /**
     * 反转 IP 地址(逆序)
     *
     * @param ipAddress IP 地址
     * @return IP 地址逆序
     */
    public static String reverseIpAddress(String ipAddress) {

        if (StringUtils.isNull(ipAddress)) {
            return null;
        }

        StringTokenizer tokenizer = new StringTokenizer(ipAddress, ".");
        List<String> addrNums = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            addrNums.add(tokenizer.nextToken());
        }

        Collections.reverse(addrNums);

        StringBuilder reverseAddress = new StringBuilder();

        for (int i = 0; i < addrNums.size(); i++) {
            if (i != addrNums.size() - 1) {
                reverseAddress.append(addrNums.get(i)).append(".");

            } else {
                reverseAddress.append(addrNums.get(i));
            }
        }

        return reverseAddress.toString();
    }

    /**
     * 获取 IP 地址的主机号
     *
     * @param ipAddress IP 地址
     * @return IP 地址的主机号
     */
    public static String getHostIdByIpAddress(String ipAddress) {

        if (!StringUtils.isNull(ipAddress) && ipAddress.contains(DOT)) {

            return ipAddress.substring(ipAddress.lastIndexOf(DOT) + 1);
        }

        return null;
    }

    /**
     * 使用 ICMP 测试网络是否可达
     *
     * @param ipAddress IP 地址
     * @param timeUnit 时间单位
     * @param timeout 超时时间
     * @return 网络是否可达
     * @throws IOException IO 异常
     */
    public static Boolean checkReachableByICMP(InetAddress ipAddress, TimeUnit timeUnit, Integer timeout) throws IOException {

        return ipAddress.isReachable((int) timeUnit.toMillis(timeout));
    }

    /**
     * 检查服务是否可用
     *
     * @param serverIpAddress 服务 IP 地址
     * @param serverPort 服务端口
     * @param timeUnit 时间单位
     * @param timeout 超时时间
     * @return 服务是否可用
     * @throws IOException IO 异常
     */
    public static Boolean checkServerIsAvailable(InetAddress serverIpAddress, Integer serverPort, TimeUnit timeUnit, Integer timeout) throws IOException {

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(serverIpAddress, serverPort), (int) timeUnit.toMillis(timeout));
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private NetWorkUtils() {}
}
