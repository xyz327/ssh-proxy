/**
 * @author xizhou
 * @date 2019/11/3 10:35
 */

module cn.xz.study.proxy {
    requires static lombok;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.io;
    //requires com.pagoda.platform.ssh;
    requires java.base;
    //requires com.jfoenix.controls;
    //requires com.jcraft.jsch;
    // 暴露包 sample 给 javafx 的模块们，使其可以在运行时使用反射访问
    opens cn.xz.study.proxy to javafx.graphics, javafx.fxml;
    opens cn.xz.study.proxy.controller to javafx.graphics, javafx.fxml;
    opens cn.xz.study.proxy.entity to com.fasterxml.jackson.databind;
}
