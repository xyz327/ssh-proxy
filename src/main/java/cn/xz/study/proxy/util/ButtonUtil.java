package cn.xz.study.proxy.util;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.experimental.UtilityClass;

/**
 * @author xizhou
 * @date 2019/11/9 19:33
 */
@UtilityClass
public class ButtonUtil {
    public void primary(Button button) {
        if(button instanceof JFXButton){
            ((JFXButton) button).setButtonType(JFXButton.ButtonType.RAISED);
        }
        button.setStyle("-fx-font-size:14px;-fx-background-color:WHITE;");
    }
    public void warn(Button button) {
        if(button instanceof JFXButton){
            ((JFXButton) button).setButtonType(JFXButton.ButtonType.RAISED);
        }
        button.setStyle("-fx-text-fill:WHITE;-fx-background-color:#FF5722;-fx-font-size:14px;");
    }
}
