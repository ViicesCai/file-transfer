package life.pisay.transfer.server.command;

/**
 * 指令工厂
 *
 * @author CAI
 * @time 2021/12/27
 */
public interface CommandFactory {

    /**
     * 获取指令
     *
     * @param commandName 指令名称
     * @return 指令对象
     */
    Command getCommand(String commandName);
}
