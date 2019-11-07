package cn.xz.study.proxy.dao;

import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xizhou
 * @since 2019/11/7 18:26
 */
@Data
public class StorageInfo {
    private Map<String, ForwardInfo> forwardInfoMap = new HashMap<>();
    private Map<String, ProxyInfo> proxyInfoMap = new HashMap<>();
}
