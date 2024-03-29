package life.pisay.transfer.server.core;

/**
 * 文件传输请求
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class FileTransferRequest {

    /**
     * 命令行
     */
    private final String line;

    /**
     * 指令
     */
    private final String command;

    /**
     * 参数
     */
    private final String args;

    public FileTransferRequest(String line) {
        this.line = line.trim();
        int index = line.indexOf(' ');
        command = parseCommand(line, index);
        args = parseArgs(line, index);
    }

    public String getLine() {

        return line;
    }

    public String getCommand() {

        return command;
    }

    public String getArgs() {

        return args;
    }

    /**
     * 解析指令
     *
     * @param line 命令行
     * @param index 分隔索引
     * @return 指令
     */
    private String parseCommand(String line, Integer index) {
        String cmd;

        // 命令行包含参数
        if (index != -1) {
            cmd = line.substring(0, index).toUpperCase();

        } else {
            cmd = line.toUpperCase();
        }

        return cmd;
    }

    /**
     * 解析参数
     *
     * @param line 命令行
     * @param index 分隔索引
     * @return 指令
     */
    private String parseArgs(String line, Integer index) {
        String args = null;

        if (index != -1) {
            args = line.substring(index + 1);

            // 无参数
            if ("".equals(args)) {
                args = null;
            }
        }

        return args;
    }
}
