package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.entity.ProxyInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 * @author xizhou
 * @date 2019/11/3 18:57
 */
public class ProxyFormController extends AbstractController {

    @FXML
    private TextField proxyDesc;
    @FXML
    private TextField proxyHost;
    @FXML
    private TextField proxyPort;
    @FXML
    private TextField proxyUsername;
    @FXML
    private TextField proxyPassword;
    @FXML
    private ToggleButton isDefaultProxy;

    protected void initializeInternal() {
        boolean anyMatch = sshService.listProxy().stream().noneMatch(ProxyInfo::getIsDefault);
        isDefaultProxy.setSelected(anyMatch);
    }

    @Override
    protected void initializeDataInternal() {

    }

    public Boolean save() {
        ProxyInfo proxyInfo = new ProxyInfo();
        proxyInfo.setDesc(proxyDesc.getText())
                .setIsDefault(isDefaultProxy.isSelected())
                .setHost(proxyHost.getText())
                .setPort(Integer.valueOf(proxyPort.getText()))
                .setUsername(proxyUsername.getText())
                .setPassword(proxyPassword.getText());
        return sshService.createProxy(proxyInfo);
    }
}
