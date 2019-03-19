package wintec.itb.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * The Bottom area of the application UI,
 * Contains a label and a button
 */
public class BottomHolder {
    private Label lblStatus;
    private Button btBrowser;
    private HBox pane;

    public BottomHolder() {
        btBrowser = new Button("Browser");
        lblStatus = new Label();

        // default information
        lblStatus.setText("No directory is selected.");

        pane = new HBox(20);
        pane.getChildren().addAll(lblStatus, btBrowser);
        pane.setPadding(new Insets(5, 5, 5, 10));
        HBox.setMargin(btBrowser, new Insets(0, 0, 0, 20));
    }

    /**
     * Sets the base URL of the server.
     *
     * @param pane The main pane that holds the application UI.
     * @param divisor The divisor used to dynamically calculate the width of the label.
     */
    public void bindWithProperty(Pane pane, double divisor) {
        lblStatus.prefWidthProperty().bind(pane.widthProperty().divide(divisor));
    }

    public Label getLblStatus() {
        return lblStatus;
    }

    public HBox getPane() {
        return pane;
    }

    public Button getBtBrowser() {
        return btBrowser;
    }
}
