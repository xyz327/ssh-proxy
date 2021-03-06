package cn.xz.study.proxy.dao;

import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xizhou
 * @since 2019/10/28 10:53
 */
//@Slf4j
public class FileSshDao implements SshDao {
    private ObjectMapper objectMapper = new ObjectMapper();
    private File proxyFileDir;
    private File forwardFileDir;
    private File storageFile;
    private StorageInfo storageInfo = new StorageInfo();
    private final Map<String, ForwardInfo> forwardInfoMap;
    private final Map<String, ProxyInfo> proxyInfoMap;
    
    public FileSshDao() {
        File dataFileDir = new File(FileUtils.getUserDirectory(), ".ssh-local");
        try {
            FileUtils.forceMkdir(dataFileDir);
            storageFile = new File(dataFileDir, "storage.json");
            if (!storageFile.exists()) {
                doSaveFile();
            }
            storageInfo = objectMapper.readValue(storageFile, StorageInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        forwardInfoMap = storageInfo.getForwardInfoMap();
        proxyInfoMap = storageInfo.getProxyInfoMap();
        
        // ===========================兼容===========================
        proxyFileDir = new File(dataFileDir, "proxy");
        forwardFileDir = new File(dataFileDir, "forward");
        
        if (forwardFileDir.exists()) {
            File[] files = forwardFileDir.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(file -> {
                    String name = file.getName();
                    try {
                        ForwardInfo forwardInfo = objectMapper.readValue(file, ForwardInfo.class);
                        forwardInfoMap.put(name, forwardInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            FileUtils.deleteQuietly(forwardFileDir);
        }
        if (proxyFileDir.exists()) {
            File[] files = proxyFileDir.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(file -> {
                    String name = file.getName();
                    try {
                        ProxyInfo proxyInfo = objectMapper.readValue(file, ProxyInfo.class);
                        proxyInfoMap.put(name, proxyInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            FileUtils.deleteQuietly(proxyFileDir);
        }
        // ===========================兼容===========================
        try {
            
            alignId();
            doSaveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void alignId() {
        proxyInfoMap.forEach((s, proxyInfo) -> {
            proxyInfo.setId(escapeId(s));
        });
        forwardInfoMap.forEach((s, forwardInfo) -> {
            forwardInfo.setId(escapeId(s));
        });
    }
    
    private void doSaveFile() throws IOException {
        objectMapper.writer(new DefaultPrettyPrinter()).writeValue(storageFile, storageInfo);
    }
    
    @Override
    public String createForward(ForwardInfo forwardInfo) {
        String id = String.format("%d-%s:%d", forwardInfo.getLocalPort(), forwardInfo.getRemoteHost(), forwardInfo.getRemotePort());
        id = escapeId(id);
        forwardInfo.setId(id);
        forwardInfoMap.put(id, forwardInfo);
        try {
            doSaveFile();
        } catch (IOException e) {
            throw new IllegalStateException("创建forwardInfo失败", e);
        }
        return id;
    }
    
    private String escapeId(String id) {
        return id;
        //return id.replaceAll("[@:\\.#\\$\\*]", "-");
    }
    
    @Override
    public ForwardInfo findForwardInfo(String id) {
        return forwardInfoMap.get(escapeId(id));
    }
    
    @Override
    public ProxyInfo findProxyInfo(String proxyId) {
        return proxyInfoMap.get(escapeId(proxyId));
    }
    
    @Override
    public Boolean deleteForward(String id) {
        System.out.println("id:" + id);
        ForwardInfo remove = forwardInfoMap.remove(escapeId(id));
        try {
            doSaveFile();
            return remove != null;
        } catch (IOException e) {
            throw new IllegalStateException("删除forwardInfo失败", e);
        }
    }
    
    @Override
    public List<ForwardInfo> listForward() {
        List<ForwardInfo> forwardInfos = forwardInfoMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        return Collections.unmodifiableList(forwardInfos);
    }
    
    @Override
    public String createProxy(ProxyInfo proxyInfo) {
        String id = String.format("%s@%s:%d", proxyInfo.getUsername(), proxyInfo.getHost(), proxyInfo.getPort());
        id = escapeId(id);
        proxyInfo.setId(id);
        proxyInfoMap.put(id, proxyInfo);
        try {
            doSaveFile();
        } catch (IOException e) {
            throw new IllegalStateException("创建proxyInfo失败", e);
        }
        return id;
    }
    
    @Override
    public boolean deleteProxy(String id) {
        System.out.println("id:" + id);
        proxyInfoMap.remove(escapeId(id));
        try {
            doSaveFile();
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("删除proxyInfo失败", e);
        }
    }
    
    @Override
    public List<ProxyInfo> listProxy() {
        List<ProxyInfo> proxyInfos = proxyInfoMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        return Collections.unmodifiableList(proxyInfos);
    }
    
    @Override
    public String getConfigJson() {
        try {
            return objectMapper.writer(new DefaultPrettyPrinter()).writeValueAsString(storageInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
    @Override
    public void fromJson(String json) {
        try {
            StorageInfo storageInfo = objectMapper.readValue(json, StorageInfo.class);
            forwardInfoMap.clear();
            forwardInfoMap.putAll(storageInfo.getForwardInfoMap());
            proxyInfoMap.clear();
            proxyInfoMap.putAll(storageInfo.getProxyInfoMap());
            alignId();
            doSaveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
