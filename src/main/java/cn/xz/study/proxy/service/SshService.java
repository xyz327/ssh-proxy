package cn.xz.study.proxy.service;

import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;

import java.util.List;

/**
 * @author xizhou
 * @since 2019/10/28 10:22
 */
public interface SshService {
    Boolean createForward(ForwardInfo forwardInfo);

    public ForwardInfo findForwardById(String id);

    List<ForwardInfo> listForward();

    Boolean deleteForward(String id);

    int start(String id);

    Boolean stop(String id);

    List<ProxyInfo> listProxy();

    Boolean createProxy(ProxyInfo proxyInfo);

    Boolean deleteProxy(String id);
}
