package life.pisay.transfer.server.core;

/**
 * 服务 Session
 *
 * @author CAI
 * @time 2021/12/28
 */
public class ServerSession {
	
    /**
     * 数据传输构造器：用于分发数据传输的端口
     */
    private final DataTransferGenerator dataTransferGenerator;
    
    public ServerSession(DataTransferGenerator dataTransferGenerator) {
        super();
        this.dataTransferGenerator = dataTransferGenerator;
    }

    public DataTransferGenerator getDataTransferGenerator() {
        return dataTransferGenerator;
    }
}
