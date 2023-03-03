package life.pisay.transfer.client.core;

/**
 * 客户端消息
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class ClientMessage {

    /**
     * 原始信息
     */
    private final String raw;

    /**
     * 消息码
     */
    private final String code;

    /**
     * 消息
     */
    private final String message;

    public ClientMessage(String msg) {
        this.raw = msg.trim();

        int index = raw.indexOf(' ');
        code = raw.substring(0, index).trim();
        message = raw.substring(index).trim();
    }

    public String getRaw() {
        return raw;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
