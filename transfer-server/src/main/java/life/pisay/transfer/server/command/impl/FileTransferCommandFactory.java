package life.pisay.transfer.server.command.impl;


import life.pisay.transfer.common.utils.StringUtils;
import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.command.CommandFactory;

import java.util.Map;

/**
 * FileTransmission 指令工厂
 *
 * @author CAI
 * @time 2021/12/27
 */
public class FileTransferCommandFactory implements CommandFactory {

    /**
     * 指令集
     */
    private final Map<String, Command> commands;

    public FileTransferCommandFactory(Map<String, Command> commands) {
        this.commands = commands;
    }

    /**
     * 获取指令
     *
     * @param commandName 指令名称
     * @return 指令对象
     */
    @Override
    public Command getCommand(String commandName) {
        if (!StringUtils.hasLength(commandName)) {

            return null;
        }

        return commands.get(commandName.toUpperCase());
    }
}
