package cn.xz.study.proxy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xizhou
 * @since 2019/10/28 10:27
 */
@Data
@Accessors(chain = true)
public class ForwardInfo {
    private String id;
    private String desc;
    private String remoteHost;
    private Integer remotePort;
    private Integer localPort;
    private String proxyId;
    private ProxyInfo proxy;
    private boolean started;
}
