package cn.xz.study.proxy.controller;

import cn.xz.study.FXMLLoaderUtil;
import cn.xz.study.proxy.FxSshService;
import cn.xz.study.proxy.entity.ForwardInfo;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
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
public class Jaria2Controller extends AbstractController {
    private FxSshService sshService = new FxSshService();
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
    
    @Override
    protected void initializeInternal() {
        tableForwardDesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDesc()));
        tableLocalPort.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getLocalPort()));
        tableRemoteHost.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRemoteHost()));
        tableRemotePort.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getRemotePort()));
        tableProxyInfo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProxyId()));
        tableOperator.setCellFactory(downloadFileVoidTableColumn -> new TableCell<ForwardInfo, Void>() {
            
            private final Button btn = new Button("Action");
            private final Button updateBtn = new Button("编辑");
            private final Button delBtn = new Button("删除");
            
            private final ButtonBar buttonBar = new ButtonBar();
            
            {
                buttonBar.getButtons().addAll(btn, updateBtn, delBtn);
                btn.setOnAction((ActionEvent event) -> {
                    ForwardInfo data = getTableView().getItems().get(getIndex());
                    ForwardInfo finded = sshService.findForwardById(data.getId());
                    String msg = data.getDesc() + "本地转发" + (finded.isStarted() ? "关闭" : "启动");
                    try {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg + "成功", new ButtonType("确定"));
                        if (finded.isStarted()) {
                            sshService.stop(finded.getId());
                            alert.setContentText(msg + "成功");
                            btn.setText("开始");
                        } else {
                            int start = sshService.start(data.getId());
                            btn.setText("停止");
                        }
                        alert.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, msg + "失败", new ButtonType("确定")).show();
                    }
                });
            }
            
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ForwardInfo data = getTableView().getItems().get(getIndex());
                    String name = "开始";
                    if (data.isStarted()) {
                        name = "暂停";
                    }
                    btn.setText(name);
                    setGraphic(buttonBar);
                }
            }
        });
        initForwardList();
    }
    
    private void initForwardList() {
        forwardTable.setItems(sshService.listObservableForward());
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
