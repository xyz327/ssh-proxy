package cn.xz.study.proxy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xizhou
 * @since 2019/10/28 10:28
 */
@Data
@Accessors(chain = true)
public class ProxyInfo {
    private String id;
    private String desc;
    private String host;
    private Integer port = 22;
    private String username;
    private String password;
    private Boolean isDefault = Boolean.FALSE;

    @Override
    public String toString() {
        return String.format("%s@%s:%d", getUsername(), getHost(), getPort());
    }
}
