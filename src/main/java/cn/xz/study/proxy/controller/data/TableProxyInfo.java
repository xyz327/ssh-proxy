package cn.xz.study.proxy.controller.data;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

/**
 * @author xizhou
 * @date 2019/11/9 17:09
 */
@Data
public class TableProxyInfo extends RecursiveTreeObject<TableProxyInfo> {
    private StringProperty id;
    private StringProperty desc;
    private StringProperty host;
    private IntegerProperty port = new SimpleIntegerProperty(22);
    private StringProperty username;
    private StringProperty password;
    private BooleanProperty isDefault;
}
