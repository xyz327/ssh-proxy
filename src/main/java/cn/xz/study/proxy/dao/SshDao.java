package cn.xz.study.proxy.dao;

import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;

import java.util.List;

/**
 * @author xizhou
 * @since 2019/10/28 10:52
 */
public interface SshDao {
    String createForward(ForwardInfo forwardInfo);
    
    ForwardInfo findForwardInfo(String id);
    
    ProxyInfo findProxyInfo(String proxyId);
    
    Boolean deleteForward(String id);
    
    List<ForwardInfo> listForward();
    
    String createProxy(ProxyInfo proxyInfo);
    
    boolean deleteProxy(String id);
    
    List<ProxyInfo> listProxy();
}
