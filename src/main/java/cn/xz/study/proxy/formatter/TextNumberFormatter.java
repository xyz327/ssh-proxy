package cn.xz.study.proxy.formatter;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * @author xizhou
 * @date 2019/11/3 20:10
 */
public class TextNumberFormatter implements UnaryOperator<TextFormatter.Change> {
    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String input = change.getText();
        if (input.matches("[0-9]*")) {
            return change;
        }
        return null;
    }
}
