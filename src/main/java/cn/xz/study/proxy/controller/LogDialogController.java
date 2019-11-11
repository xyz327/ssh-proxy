package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.App;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * @author xizhou
 * @since 2019/11/11 15:44
 */
public class LogDialogController extends AbstractController {
    @FXML
    private TextArea logText;
    
    @Override
    protected void initializeInternal() {
    
    }
    
    @Override
    protected void initializeDataInternal() {
        logText.setText(App.stringWriter.toString());
    }
}
