package life.pisay.transfer.plugin.file.listener.core.impl;

import java.io.File;
import java.util.Objects;

/**
 * 传输 DTO
 *
 * @author Viices Cai
 * @time 2022/6/30
 */
public class TransferDTO {

    /**
     * 传输文件
     */
    private File file;

    /**
     * 是否传输失败
     */
    private Boolean failure = false;

    public TransferDTO() { }

    public TransferDTO(File file) {
        this.file = file;
    }

    public TransferDTO(File file, Boolean failure) {
        this.file = file;
        this.failure = failure;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean isFailure() {
        return failure;
    }

    public void setFailure(Boolean failure) {
        this.failure = failure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransferDTO that = (TransferDTO) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
