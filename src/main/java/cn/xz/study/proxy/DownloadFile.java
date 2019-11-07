package cn.xz.study.proxy;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xizhou
 * @date 2019/11/3 11:58
 */
@Data
@Accessors(chain = true)
public class DownloadFile {
    private String name;
    private DownloadStatus status;
    private Long size;
    private long downloadedSize;
    private String savedFile;
}
