package cn.xz.study.proxy.ssh;

import com.jcraft.jsch.Logger;

/**
 * @author xizhou
 * @since 2019/10/24 17:15
 */
public class JSchSlf4jLogger implements Logger {
    private final org.slf4j.Logger logger;
    
    public JSchSlf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public boolean isEnabled(int level) {
        switch (level) {
            case Logger.DEBUG:
                return logger.isDebugEnabled();
            case Logger.INFO:
                return logger.isInfoEnabled();
            case Logger.WARN:
                return logger.isWarnEnabled();
            case Logger.ERROR:
            case Logger.FATAL:
                return logger.isErrorEnabled();
            default:
                return false;
        }
    }
    
    @Override
    public void log(int level, String message) {
        switch (level) {
            case Logger.DEBUG:
                logger.debug(message);
                break;
            case Logger.INFO:
                logger.info(message);
                break;
            case Logger.WARN:
                logger.warn(message);
                break;
            case Logger.ERROR:
            case Logger.FATAL:
                logger.error(message);
                break;
            default:
            
        }
    }
}
