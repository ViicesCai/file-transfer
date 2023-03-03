package life.pisay.transfer.server.command.impl;

import life.pisay.transfer.common.utils.FileUtils;
import life.pisay.transfer.common.utils.ParameterFormatter;
import life.pisay.transfer.server.command.Command;
import life.pisay.transfer.server.connect.FileTransferConnection;
import life.pisay.transfer.server.enums.Reply;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 寻求文件
 * 
 * @author Viices Cai
 * @time 2022/01/26
 */
public class Seek implements Command {

	@Override
	public String name() {
		return "seek".toUpperCase();
	}

	@Override
	public Boolean execute(FileTransferConnection connection, String args) throws IOException {

		if (args == null) {
			connection.send(Reply.REPLY_500, "Syntax error in parameters or arguments.");
			return true;
		}

		try {
			Map<String, String> params = new ParameterFormatter(args).getParams();

			if (!params.containsKey("filePath") || !params.containsKey("fileName")) {
            	connection.send(Reply.REPLY_500, "Syntax error in parameters or arguments.");
                return true;
			}

			String filePath = params.get("filePath");
			if ("root".equals(filePath)) {
				filePath = FileUtils.getDesktopDirPath() + File.separator;
			}

			File file = new File(filePath + params.get("fileName"));
			if (!file.exists()) {
				connection.send(Reply.REPLY_400);
				return true;
			}
			connection.send(Reply.REPLY_200, "fileName:" + file.getName() + ",filePath:" + file.getParent() + ",fileSize:" + FileUtils.getFileRealSize(file));

		} catch (Exception e) {
			connection.send(Reply.REPLY_500, "IOException during data transfer");
		}
		
		return true;
	}
}
