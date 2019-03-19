package wintec.itb.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * The Top area of the application UI,
 * Contains a search box and a button
 */
public class TopHolder {
    private TextField queryInput;
    private Button btSearch;
    private HBox pane;

    public TopHolder() {
        this.queryInput = new TextField();
        this.btSearch = new Button("Search");

        this.pane = new HBox(20);
        pane.getChildren().addAll(queryInput, btSearch);
        pane.setPadding(new Insets(5, 5, 5, 10));
        HBox.setMargin(btSearch, new Insets(0, 0, 0, 20));
    }

    /**
     * Sets the base URL of the server.
     *
     * @param pane The main pane that holds the application UI.
     * @param divisor The divisor used to dynamically calculate the width of the search box.
     */
    public void bindWidthProperty(Pane pane, double divisor) {
        queryInput.prefWidthProperty().bind(pane.widthProperty().divide(divisor));
    }

    public TextField getQueryInput() {
        return queryInput;
    }

    public Button getBtSearch() {
        return btSearch;
    }

    public HBox getPane() {
        return pane;
    }
}
