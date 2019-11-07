package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.FXMLLoaderUtil;
import cn.xz.study.proxy.FxSshService;
import cn.xz.study.proxy.controller.ui.ForwardTableOperatorCellFactory;
import cn.xz.study.proxy.entity.ForwardInfo;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * @author xizhou
 * @date 2019/11/3 12:25
 */
public class MainController extends AbstractController {
    private FxSshService sshService = FxSshService.INSTANCE;
    @FXML
    private TableView<ForwardInfo> forwardTable;
    @FXML
    private TableColumn<ForwardInfo, String> tableForwardDesc;
    @FXML
    private TableColumn<ForwardInfo, Number> tableLocalPort;
    @FXML
    private TableColumn<ForwardInfo, Number> tableRemotePort;
    @FXML
    private TableColumn<ForwardInfo, String> tableRemoteHost;
    @FXML
    private TableColumn<ForwardInfo, String> tableProxyInfo;
    @FXML
    private TableColumn<ForwardInfo, Void> tableOperator;
    @FXML
    private Label infoLabel;
    @Override
    protected void initializeInternal() {
        initInfoLabel();
        tableForwardDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesc()));
        tableLocalPort.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getLocalPort()));
        tableRemoteHost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemoteHost()));
        tableRemotePort.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getRemotePort()));
        tableProxyInfo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProxyId()));
        tableOperator.setCellFactory(new ForwardTableOperatorCellFactory(this));
        forwardTable.setItems(sshService.listObservableForward());
    }

    @Override
    protected void initializeDataInternal() {
        initForwardList();
    }

    private void initInfoLabel() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        infoLabel.setText(String.format("power by java :%s javafx: %s", javaVersion, javafxVersion));
    }

    private void initForwardList() {
        sshService.listObservableForward();
    }
    
    //======================= EVENT Handler
    
    public void onAddFile(Event event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("新增转发");
        FXMLLoader fxmlLoader = FXMLLoaderUtil.loadResource("forwardForm.fxml");
        alert.setDialogPane(fxmlLoader.load());
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.get().getButtonData().isCancelButton()) {
            System.out.println("确认");
            ForwardFormController controller = fxmlLoader.getController();
            controller.save();
            initForwardList();
        }
    }
}
