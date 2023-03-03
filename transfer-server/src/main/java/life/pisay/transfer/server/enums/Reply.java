package life.pisay.transfer.server.enums;

/**
 * 响应码
 * 
 * @author Viices Cai
 * @time 2022/01/27
 */
public enum Reply {

	/**
     * 打开数据连接
     */
    REPLY_150(150, "Data Connection Address : {}"),
    
    /**
     * 命令完成
     */
    REPLY_200(200, "{} Command okay."),

    /**
     * 连接：已注销
     */
    REPLY_221(221, "Service closing control connection."),

    /**
     * 文件不存在
     */
    REPLY_400(400, "The file does no exist."),

    /**
     * 服务异常
     */
    REPLY_500(500, "Server error.");
    
    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final String info;

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static Reply getValue(int code) {
        for (Reply reply : Reply.values()) {
            if (reply.code == code) {
                return reply;
            }
        }

        throw new IllegalArgumentException("Unknown reply : " + code);
    }

    Reply(int code, String info) {
        this.code = code;
        this.info = info;
    }
}
