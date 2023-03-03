package life.pisay.transfer.server.command.impl;

import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.command.CommandFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileTransmission 指令构造器
 *
 * @author CAI
 * @time 2021/12/27
 */
public class FileTransferCommandFactoryConstructor {

    /**
     * 默认指令集
     */
    private static final List<Command> DEFAULT_COMMANDS = new ArrayList<>();

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * 初始化默认指令集
     */
    static {
        DEFAULT_COMMANDS.add(new Transfer());
        DEFAULT_COMMANDS.add(new Quit());
        DEFAULT_COMMANDS.add(new Seek());
        DEFAULT_COMMANDS.add(new Restore());
    }

    /**
     * 创建指令工厂
     *
     * @return 指令工厂
     */
    public CommandFactory createCommandFactory() {

        for (Command command : DEFAULT_COMMANDS) {
            commands.put(command.name(), command);
        }

        return new FileTransferCommandFactory(commands);
    }
}
