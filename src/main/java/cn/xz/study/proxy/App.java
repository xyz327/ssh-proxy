package cn.xz.study.proxy;

import cn.xz.study.proxy.controller.MainController;
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
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
        Parent parent = fxmlLoader.load();
        MainController controller  = fxmlLoader.getController();
        controller.setApplication(this);

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
