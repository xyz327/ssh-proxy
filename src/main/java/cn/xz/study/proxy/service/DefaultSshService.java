package cn.xz.study.proxy.service;

import com.pagoda.platform.ssh.JProxy;
import cn.xz.study.proxy.dao.FileSshDao;
import cn.xz.study.proxy.dao.SshDao;
import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xizhou
 * @since 2019/10/28 10:47
 */
public class DefaultSshService implements SshService {
    private Map<String, JProxy> startedProxyMap = new HashMap<>();
    private Map<String, Set<String>> proxyStartedForward = new HashMap<>();

    private SshDao sshDao = new FileSshDao();

    @Override
    public Boolean createForward(ForwardInfo forwardInfo) {
        String id = sshDao.createForward(forwardInfo);
        return true;
    }

    @Override
    public ForwardInfo findForwardById(String id) {
        ForwardInfo forwardInfo = sshDao.findForwardInfo(id);
        determineForwardStatus(forwardInfo);
        return forwardInfo;
    }

    private void determineForwardStatus(ForwardInfo forwardInfo) {
        proxyStartedForward.forEach((proxyId, forwardIds) -> {
            if (forwardIds.contains(forwardInfo.getId())) {
                forwardInfo.setStarted(true);
            }
        });
    }

    @Override
    public List<ForwardInfo> listForward() {
        List<ForwardInfo> forwardInfos = sshDao.listForward().stream().filter(Objects::nonNull).collect(Collectors.toList());
        Map<String, ForwardInfo> forwardInfoMap = forwardInfos.stream().collect(Collectors.toMap(ForwardInfo::getId, o -> o));
        proxyStartedForward.forEach((proxyId, forwardIds) -> {
            forwardIds.forEach(forwardId -> forwardInfoMap.get(forwardId).setStarted(true));
        });
        return forwardInfos;
    }

    @Override
    public Boolean deleteForward(String id) {
        ForwardInfo forwardInfo = sshDao.findForwardInfo(id);
        return sshDao.deleteForward(id);
    }

    @Override
    public int start(String id) {
        ForwardInfo forwardInfo = sshDao.findForwardInfo(id);
        String proxyId = forwardInfo.getProxyId();
        JProxy proxy = startedProxyMap.computeIfAbsent(proxyId, key -> {
            ProxyInfo proxyInfo = sshDao.findProxyInfo(proxyId);
            JProxy jProxy = new JProxy(proxyInfo.getHost(), proxyInfo.getUsername(), proxyInfo.getPassword(), proxyInfo.getPort());
            jProxy.start();
            return jProxy;
        });
        if (!proxy.isRunning()) {
            proxy.start();
        }
        int forwardLocal = proxy.forwardLocal(forwardInfo.getLocalPort(), forwardInfo.getRemoteHost(), forwardInfo.getRemotePort());
        proxyStartedForward.computeIfAbsent(proxyId, k -> new HashSet<>()).add(id);
        return forwardLocal;

    }

    @Override
    public Boolean stop(String id) {
        ForwardInfo forwardInfo = sshDao.findForwardInfo(id);
        JProxy jProxy = startedProxyMap.get(forwardInfo.getProxyId());
        if (jProxy == null || !jProxy.isRunning()) {
            //TODO  proxy都没有开启
            return true;
        }
        jProxy.stopForwardLocal(forwardInfo.getLocalPort());
        Set<String> localPorts = proxyStartedForward.getOrDefault(forwardInfo.getProxyId(), new HashSet<>());
        localPorts.remove(id);
        if (localPorts.isEmpty()) {
            System.out.println("没有端口转发了,准备停止proxy" + String.format("%s@%s:%s", jProxy.getSshUsername(), jProxy.getSshHost(), jProxy.getSshPort()));
            jProxy.stop();
            startedProxyMap.remove(forwardInfo.getProxyId());
        }
        return true;
    }

    @Override
    public List<ProxyInfo> listProxy() {
        List<ProxyInfo> proxyInfos = sshDao.listProxy().stream().filter(Objects::nonNull).collect(Collectors.toList());
        return proxyInfos;
    }

    @Override
    public Boolean createProxy(ProxyInfo proxyInfo) {
        String id = sshDao.createProxy(proxyInfo);
        return true;
    }

    @Override
    public Boolean deleteProxy(String id) {
        sshDao.deleteProxy(id);
        return true;
    }
}
