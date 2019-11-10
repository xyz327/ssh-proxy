package cn.xz.study.proxy.controller.ui;

import cn.xz.study.proxy.FxSshService;
import cn.xz.study.proxy.controller.AbstractController;
import cn.xz.study.proxy.controller.ProxyListController;
import cn.xz.study.proxy.entity.ProxyInfo;
import cn.xz.study.proxy.util.ButtonUtil;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.*;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author xizhou
 * @date 2019/11/7 23:03
 */
@RequiredArgsConstructor
//public class ProxyTableOperatorCellFactory implements Callback<TableColumn<ProxyInfo, Void>, TableCell<ProxyInfo, Void>> {
public class ProxyTableOperatorCellFactory implements Callback<TableColumn<ProxyInfo, Void>, TableCell<ProxyInfo, Void>> {
    private FxSshService sshService = FxSshService.INSTANCE;
    private final AbstractController controller;

    @Override
    public TableCell<ProxyInfo, Void> call(TableColumn<ProxyInfo, Void> forwardInfoVoidTableColumn) {
        return new TableCell<ProxyInfo, Void>() {

            private final JFXButton updateBtn = new JFXButton("编辑");
            private final JFXButton delBtn = new JFXButton("删除");
            private final JFXButton setDefaultBtn = new JFXButton("设为默认");

            private final ButtonBar buttonBar = new ButtonBar();
            //   private HBox hBox = new HBox(10);

            {

                updateBtn.setButtonType(JFXButton.ButtonType.RAISED);
                delBtn.setButtonType(JFXButton.ButtonType.RAISED);
                ButtonUtil.primary(setDefaultBtn);
                ButtonUtil.warn(delBtn);
                buttonBar.getButtons().addAll(setDefaultBtn, delBtn);
                setDefaultBtn.setOnAction(event -> {
                    ProxyInfo data = getTableView().getItems().get(getIndex());
                    System.out.println(data.toString());
                    data.setIsDefault(true);
                    sshService.createProxy(data);
                    ((ProxyListController) controller).getProxyTable().refresh();
                });
                //  hBox.getChildren().addAll(updateBtn, delBtn);
                delBtn.setOnAction(event -> {
                    Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "确定删除?", ButtonType.OK, ButtonType.CANCEL).showAndWait();
                    if (buttonType.get().getButtonData().isCancelButton()) {
                        return;
                    }
                    ProxyInfo data = getTableView().getItems().get(getIndex());
                    try {

                        if (sshService.deleteProxy(data.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "删除成功", new ButtonType("确定")).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.INFORMATION, "删除失败", new ButtonType("确定")).show();
                    }
                    controller.initializeData();
                });
                updateBtn.setOnAction(event -> {
                    ProxyInfo data = getTableView().getItems().get(getIndex());
                    // ProxyInfo finded = sshService.findForwardById(data.getId());
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ProxyInfo data = getTableView().getItems().get(getIndex());
                    if (data.getIsDefault()) {
                        buttonBar.getButtons().removeAll(setDefaultBtn);
                    }
                    setGraphic(buttonBar);
                }
            }
        };
    }
}
