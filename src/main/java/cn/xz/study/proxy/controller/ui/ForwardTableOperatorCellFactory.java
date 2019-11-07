package cn.xz.study.proxy.controller.ui;

import cn.xz.study.proxy.FxSshService;
import cn.xz.study.proxy.controller.AbstractController;
import cn.xz.study.proxy.entity.ForwardInfo;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;

/**
 * @author xizhou
 * @date 2019/11/7 23:03
 */
@RequiredArgsConstructor
public class ForwardTableOperatorCellFactory implements Callback<TableColumn<ForwardInfo, Void>, TableCell<ForwardInfo, Void>> {
    private FxSshService sshService = FxSshService.INSTANCE;
    private final AbstractController controller;
    @Override
    public TableCell<ForwardInfo, Void> call(TableColumn<ForwardInfo, Void> forwardInfoVoidTableColumn) {
        return new TableCell<ForwardInfo, Void>() {

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
                delBtn.setOnAction(event -> {
                    ForwardInfo data = getTableView().getItems().get(getIndex());
                    try {
                        if (sshService.deleteForward(data.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "删除成功", new ButtonType("确定")).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.INFORMATION, "删除失败", new ButtonType("确定")).show();
                    }
                    controller.initializeData();
                });
                updateBtn.setOnAction(event -> {
                    ForwardInfo data = getTableView().getItems().get(getIndex());
                    ForwardInfo finded = sshService.findForwardById(data.getId());

                    controller.initializeData();
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
        };
    }
}
