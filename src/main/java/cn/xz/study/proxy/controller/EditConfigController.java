package cn.xz.study.proxy.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


/**
 * @author xizhou
 * @date 2019/11/9 15:02
 */
public class EditConfigController extends AbstractController {

    @FXML
    private TextArea configEditer;

    @Override
    protected void initializeInternal() {

        String json = sshService.getConfigJson();
        configEditer.setText(json);

    }

    @Override
    protected void initializeDataInternal() {
    }

    public void save() {
        sshService.fromJson(configEditer.getText());
    }
}
