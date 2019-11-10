package cn.xz.study.proxy.util;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import lombok.experimental.UtilityClass;

/**
 * @author xizhou
 * @date 2019/11/9 19:50
 */
@UtilityClass
public class DialogUtil {

    public void showDialog(String content){
        JFXDialog jfxDialog = new JFXDialog();
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Label(content));
        jfxDialog.getChildren().addAll(jfxDialogLayout);
        jfxDialog.show();
    }
}
