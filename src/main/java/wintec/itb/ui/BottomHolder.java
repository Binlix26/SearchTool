package wintec.itb.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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


//        btBrowser.setOnAction(event -> handleBrowse());
    }

    public void bindWithProperty(Pane board, double metric) {
        lblStatus.prefWidthProperty().bind(board.widthProperty().divide(metric));
    }

    public Label getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(Label lblStatus) {
        this.lblStatus = lblStatus;
    }

    public HBox getPane() {
        return pane;
    }

    public Button getBtBrowser() {
        return btBrowser;
    }
}
