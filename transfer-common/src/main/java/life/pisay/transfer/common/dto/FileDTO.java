package life.pisay.transfer.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * File DTO
 *
 * @author Viices Cai
 * @time 2022/6/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDTO {

    private String name;

    private String path;

    private Long size;
}
