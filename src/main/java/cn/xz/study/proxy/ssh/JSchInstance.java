package cn.xz.study.proxy.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xizhou
 * @since 2019/10/30 11:15
 */
public enum JSchInstance {
    /**
     * 实例
     */
    INSTANCE;
    @Getter
    private Map<String, String> defaultConfig = new HashMap<>();
    
    @Delegate
    private JSch jSch = new JSch();
    
    {
        defaultConfig.put("StrictHostKeyChecking", "no");
        JSch.setLogger(new JSchSlf4jLogger(LoggerFactory.getLogger(JSch.class)));
    }
    
    public void applyDefaultConfig(Session session) {
        defaultConfig.forEach(session::setConfig);
    }
}
