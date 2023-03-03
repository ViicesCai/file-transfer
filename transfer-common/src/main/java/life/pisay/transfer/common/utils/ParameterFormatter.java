package life.pisay.transfer.common.utils;

import life.pisay.transfer.common.dto.FileDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 参数格式化工具
 * 
 * @author Viices Cai
 * @time 2022/01/27
 */
public class ParameterFormatter {

	/**
	 * 包含参数的字符串
	 */
	private final String line;
	
	/**
	 * 参数集
	 */
	private final Map<String, String> params;
	
	public ParameterFormatter(String line) {
		this.line = line;
		
		if (!StringUtils.hasText(line)) {
			throw new IllegalArgumentException();
		}
		
		StringTokenizer tokenizer = new StringTokenizer(line, ",");
        params = new HashMap<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.contains(":")) {
                int index = token.indexOf(":");
                String key = token.substring(0, index);
                String value = token.substring(index + 1, token.length());

                params.put(key, value);
             }
         }
	}

	/**
	 * 获取参数集合
	 * 
	 * @return 参数集合
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * 获取原始字符串
	 * 
	 * @return 字符串
	 */
	public String getRaw() {
		return line;
	}

	/**
	 * 转为 FileDTO
	 *
	 * @return FileDTO
	 */
	public FileDTO toFileDTO() {

		FileDTO fileDTO = new FileDTO();
		if (params.containsKey("fileName")) {
			fileDTO.setName(params.get("fileName"));
		}

		if (params.containsKey("filePath")) {
			fileDTO.setPath(params.get("filePath"));
		}

		if (params.containsKey("fileSize")) {
			fileDTO.setSize(Long.parseLong(params.get("fileSize")));
		}

		return fileDTO;
	}
}
