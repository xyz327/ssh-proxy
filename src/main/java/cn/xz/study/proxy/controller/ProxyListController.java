package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.FXMLLoaderUtil;
import cn.xz.study.proxy.controller.ui.ProxyTableOperatorCellFactory;
import cn.xz.study.proxy.entity.ProxyInfo;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

import java.io.IOException;
import java.util.Optional;

/**
 * @author xizhou
 * @date 2019/11/9 15:02
 */
public class ProxyListController extends AbstractController {
    @FXML
    @Getter
    private TableView<ProxyInfo> proxyTable;
    @FXML
    private TableColumn<ProxyInfo, String> tableProxyDesc;
    @FXML
    private TableColumn<ProxyInfo, Boolean> tableProxyDefault;
    @FXML
    private TableColumn<ProxyInfo, String> tableProxyHost;
    @FXML
    private TableColumn<ProxyInfo, Number> tableProxyPort;
    @FXML
    private TableColumn<ProxyInfo, String> tableProxyUsername;
    @FXML
    private TableColumn<ProxyInfo, String> tableProxyPassword;
    @FXML
    private TableColumn<ProxyInfo, Void> tableProxyOperator;

    @Override
    protected void initializeInternal() {

        tableProxyDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesc()));
        tableProxyDefault.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getIsDefault()));
        tableProxyHost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHost()));
        tableProxyPort.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getPort()));
        tableProxyUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        tableProxyPassword.setCellValueFactory(cellData -> new SimpleStringProperty("******"));
        //tableProxyPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        tableProxyOperator.setCellFactory(new ProxyTableOperatorCellFactory(this));
        // proxyTablesshService.listObservableProxy());

        proxyTable.setItems(sshService.listObservableProxy());

    }

    @Override
    protected void initializeDataInternal() {
        initProxyList();
    }

    private void initProxyList() {
        sshService.listObservableProxy();
    }


    public void onAddProxy(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("新增代理");
        FXMLLoader fxmlLoader = FXMLLoaderUtil.loadResource("proxyForm.fxml");
        try {
            alert.setDialogPane(fxmlLoader.load());
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (!buttonType.get().getButtonData().isCancelButton()) {
                ProxyFormController proxyFormController = fxmlLoader.getController();
                if (proxyFormController.save()) {
                    initializeData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
