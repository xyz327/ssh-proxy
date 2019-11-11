package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.FXMLLoaderUtil;
import cn.xz.study.proxy.FxSshService;
import cn.xz.study.proxy.controller.ui.ForwardTableOperatorCellFactory;
import cn.xz.study.proxy.entity.ForwardInfo;
import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * @author xizhou
 * @date 2019/11/3 12:25
 */
//@ViewController(value = "/main.fxml", title = "本地端口代理转发管理")
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
        // 编辑
        forwardTable.setEditable(true);
    }

    @Override
    protected void initializeDataInternal() {
        initForwardList();
    }

    private void initInfoLabel() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        infoLabel.setText(String.format("build by java :%s javafx: %s", javaVersion, javafxVersion));
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

    public void onShowProxyList(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = FXMLLoaderUtil.loadResource("proxyList.fxml");
        AnchorPane anchorPane = fxmlLoader.load();
        Stage stage = new Stage();
        JFXDecorator jfxDecorator = new JFXDecorator(stage, anchorPane);
        Scene scene = new Scene(jfxDecorator);
        stage.setScene(scene);
        stage.setTitle("跳板机管理");
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.showAndWait();
    }

    public void onShowEditConfg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = FXMLLoaderUtil.loadResource("editConfig.fxml");
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setDialogPane(fxmlLoader.load());
        alert.setTitle("编辑配置文件");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.get().getButtonData().isCancelButton()) {
            EditConfigController controller = fxmlLoader.getController();
            controller.save();
            initForwardList();
        }
    }
}
