package wintec.itb.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import wintec.itb.core.InvertedIndex;

import java.io.File;

public class UIService {
    // Singleton to make sure only one instance is active
    private static final UIService uiService = new UIService();

    private UIService() {
    }

    public static UIService getInstance() {
        return uiService;
    }

    public void handleBrowse(InvertedIndex invertedIndex, Label label) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(null);

        if (dir != null) {
            String status = dir.getAbsolutePath();

            // never affects the main Thread responding the UI event
            new Thread(() -> {
                // make sure the inverted index only contains the file under the current directory
                // clear the records for previously chosen directory
                if (!invertedIndex.isEmpty()) {
                    invertedIndex.reset();
                }

                // get inverted index for the files under the chosen directory
                invertedIndex.getFilesForInvertedIndex(dir);

                Platform.runLater(() -> {
                    label.setText(status);
                });

                invertedIndex.displayInvertedIndex();
                invertedIndex.displayFileMap();
            }).start();
        }
    }

    public void handleSearch() {

    }

    public void handleUpdate() {

    }

}
