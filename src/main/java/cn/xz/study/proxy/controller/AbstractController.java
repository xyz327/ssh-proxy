package cn.xz.study.proxy.controller;

import cn.xz.study.proxy.FxSshService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xizhou
 * @date 2019/11/3 12:34
 */
public abstract class AbstractController {
    protected FxSshService sshService = FxSshService.INSTANCE;
    @Setter
    @Getter
    protected Application application;

    @FXML
    protected void initialize() {
        initializeInternal();
        initializeData();
    }

    protected abstract void initializeInternal();

    protected abstract void initializeDataInternal();

    public void initializeData() {
        initializeDataInternal();
    }


    //
    public TextFormatter.Change onCheckNumber(TextFormatter.Change change) {
        String input = change.getText();
        if (input.matches("[0-9]*")) {
            return change;
        }
        return null;
    }
}
