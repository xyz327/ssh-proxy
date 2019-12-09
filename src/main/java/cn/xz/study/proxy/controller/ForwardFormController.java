package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.FXMLLoaderUtil;
import cn.xz.study.proxy.entity.ForwardInfo;
import cn.xz.study.proxy.entity.ProxyInfo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xizhou
 * @date 2019/11/3 18:57
 */
public class ForwardFormController extends AbstractController {
    
    @FXML
    private TextField forwardDesc;
    @FXML
    private TextField forwardLocalPort;
    @FXML
    private TextField forwardRemoteHost;
    @FXML
    private TextField forwardRemotePort;
    @FXML
    private ChoiceBox<ProxyInfo> forwardProxyInfo;
    
    @Override
    protected void initializeInternal() {
    
    }
    
    private void initProxyList() {
        List<ProxyInfo> proxyInfos = sshService.listProxy();
        List<String> stringList =
            proxyInfos.stream().map(proxyInfo -> proxyInfo.getDesc() + proxyInfo.toString()).collect(Collectors.toList());
        forwardProxyInfo.setItems(FXCollections.observableArrayList(proxyInfos));
        if (!stringList.isEmpty()) {
            Optional<ProxyInfo> defaultProxy = sshService.findDefaultProxy();
            if (defaultProxy.isPresent()) {
                ProxyInfo proxyInfo = defaultProxy.get();
                forwardProxyInfo.setValue(proxyInfo);
            } else {
                forwardProxyInfo.setValue(proxyInfos.get(0));
            }
        }
    }
    
    @Override
    protected void initializeDataInternal() {
        initProxyList();
    }
    
    //==================
    
    public boolean onNewProxy(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("新增代理");
        FXMLLoader fxmlLoader = FXMLLoaderUtil.loadResource("proxyForm.fxml");
        alert.setDialogPane(fxmlLoader.load());
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.get().getButtonData().isCancelButton()) {
            System.out.println("确认");
            ProxyFormController proxyFormController = fxmlLoader.getController();
            if (proxyFormController.save()) {
                initializeData();
                return true;
            }
            return false;
            
        }
        return true;
    }
    
    public void save() {
        ForwardInfo forwardInfo = new ForwardInfo();
        forwardInfo.setDesc(forwardDesc.getText())
            .setLocalPort(Integer.valueOf(forwardLocalPort.getText()))
            .setRemoteHost(forwardRemoteHost.getText())
            .setRemotePort(Integer.valueOf(forwardRemotePort.getText()))
            .setProxyId(forwardProxyInfo.getValue().getId());
        sshService.createForward(forwardInfo);
    }
}
