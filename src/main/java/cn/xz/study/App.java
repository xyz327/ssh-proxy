package cn.xz.study;

import cn.xz.study.proxy.controller.Jaria2Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("sample.fxml")));
        Parent parent = fxmlLoader.load();
        Jaria2Controller controller  = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(parent, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
