package life.pisay.transfer.server.exception;

/**
 * 数据传输异常
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
public class DataConnectionException extends Exception {

    public DataConnectionException() {
        super();
    }

    public DataConnectionException(String msg) {
        super(msg);
    }

    public DataConnectionException(String msg, Throwable th) {
        super(msg, th);
    }
}
