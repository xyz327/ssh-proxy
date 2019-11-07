package cn.xz.study.proxy;

import javafx.fxml.FXMLLoader;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author xizhou
 * @date 2019/11/3 18:42
 */
@UtilityClass
public class FXMLLoaderUtil {
    public FXMLLoader loadResource(String fxml){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation( Objects.requireNonNull(FXMLLoaderUtil.class.getClassLoader().getResource(fxml)));
        return fxmlLoader;
    }
}
