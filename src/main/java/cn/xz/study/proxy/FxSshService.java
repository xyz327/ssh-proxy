package cn.xz.study.proxy;

import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;
import cn.xz.study.proxy.service.DefaultSshService;
import cn.xz.study.proxy.service.SshService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.experimental.Delegate;

/**
 * @author xizhou
 * @date 2019/11/3 18:20
 */
public class FxSshService {
    @Delegate
    private SshService sshService = new DefaultSshService();
    private ObservableList<ForwardInfo> observableList = FXCollections.observableArrayList();
    private ObservableList<ProxyInfo> proxyInfoObservableList = FXCollections.observableArrayList();

    public ObservableList<ForwardInfo> listObservableForward() {
        observableList.clear();
        observableList.addAll(sshService.listForward());
        return observableList;
    }

    public ObservableList<ProxyInfo> listObservableProxy() {
        proxyInfoObservableList.addAll(sshService.listProxy());
        return proxyInfoObservableList;
    }


}
