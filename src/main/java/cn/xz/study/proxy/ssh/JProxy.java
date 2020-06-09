package cn.xz.study.proxy.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xizhou
 * @since 2019/10/29 15:16
 */
@Getter
public class JProxy {
    private final String sshHost;
    private final String sshUsername;
    private final String sshPassword;
    @Getter
    private int sshPort = 22;
    @Setter
    private int connectTimeout = 0;
    @Setter
    private Map<String, String> config = new HashMap<>();
    private Session proxySession;
    
    public JProxy(String sshHost, String sshUsername, String sshPassword, int sshPort) {
        this.sshHost = sshHost;
        this.sshUsername = sshUsername;
        this.sshPassword = sshPassword;
        this.sshPort = sshPort;
    }
    
    public JProxy(String sshHost, String sshUsername, String sshPassword) {
        this(sshHost, sshUsername, sshPassword, 22);
    }
    
    public void start() {
        try {
            proxySession = JSchInstance.INSTANCE.getSession(sshUsername, sshHost, sshPort);
            proxySession.setPassword(sshPassword);
            JSchInstance.INSTANCE.applyDefaultConfig(proxySession);
            if (config != null) {
                config.forEach(proxySession::setConfig);
            }
            proxySession.connect(connectTimeout);
        } catch (JSchException e) {
            throw new IllegalStateException(String.format("ssh代理:[%s@%s:%d]启动失败", sshUsername, sshHost, sshPort), e);
        }
    }
    
    public int forwardLocal(int localPort, String remoteHost, int remotePort) {
        try {
            int assinged_port = proxySession.setPortForwardingL(localPort, remoteHost, remotePort);
            System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
            return assinged_port;
        } catch (JSchException e) {
            throw new IllegalStateException(String.format("ssh代理开启转发[localhost:%d->%s:%s]失败", localPort, remoteHost, remotePort), e);
        }
    }
    
    public void stopForwardLocal(int localPort) {
        try {
            proxySession.delPortForwardingL(localPort);
            System.out.println("关闭本地转发localhost:" + localPort);
        } catch (JSchException e) {
            throw new IllegalStateException(String.format("ssh代理关闭转发[localhost:%d]失败", localPort), e);
        }
    }
    
    public void stop() {
        if (isRunning()) {
            proxySession.disconnect();
        }
    }
    
    public boolean isRunning() {
        return proxySession.isConnected();
    }
}
