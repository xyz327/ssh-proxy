package cn.xz.study.proxy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author xizhou
 * @date 2019/11/3 14:30
 */
public class DownloadFileService {
    private ObservableList<DownloadFile> downloadFiles = FXCollections.observableArrayList();
    public ObservableList<DownloadFile> listDownloadFile() {
        return downloadFiles;
    }

    public void addDownloadFile(DownloadFile downloadFile) {
        downloadFiles.add(downloadFile);
    }
}
