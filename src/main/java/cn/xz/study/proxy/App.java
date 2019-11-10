package cn.xz.study.proxy;

import cn.xz.study.proxy.controller.MainController;
import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import demos.MainDemo;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Hello world!
 */
public class App extends Application {
    public static Stage mainStage;
    private static File lockFile;
    private static transient AtomicBoolean started = new AtomicBoolean(false);

    static {
        File userDirectory = FileUtils.getUserDirectory();
        File dataDir = new File(userDirectory, ".ssh-local");
        try {
            FileUtils.forceMkdir(dataDir);
            lockFile = new File(dataDir, "run.lock");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("====================");
                if (started.get()) {
                    try {
                        FileUtils.forceDelete(lockFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void checkInstance() {
        if (lockFile != null) {
            if (lockFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "程序已经在运行!", ButtonType.OK, new ButtonType("继续运行新程序", ButtonBar.ButtonData.APPLY));
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().getTypeCode().equals(ButtonBar.ButtonData.APPLY.getTypeCode())) {
                    return;
                }
                System.exit(0);
            }
        }
    }
/*
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
    }*/

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @Override
    public void stop() throws Exception {
        FileUtils.deleteQuietly(lockFile);
    }

    @Override
    public void start(Stage stage) throws Exception {

        checkInstance();
        if (lockFile != null) {
            try {
                FileUtils.touch(lockFile);
                FileUtils.forceDeleteOnExit(lockFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        started.set(true);
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(MainDemo.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new SVGGlyph(""));

        stage.setTitle("本地端口代理转发管理");

        double width = 800;
        double height = 400;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 2.5;
            height = bounds.getHeight() / 2;
        } catch (Exception e) {
        }

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                MainDemo.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        MySystemTray.getInstance().listen(stage);
        stage.show();
        mainStage = stage;
    }
}
