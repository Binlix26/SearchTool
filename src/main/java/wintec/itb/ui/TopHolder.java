package wintec.itb.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

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


        btSearch.setOnAction(event -> {
            try {
//                handleSearch();
                throw new SQLException();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void bindWidthProperty(Pane pane, double metric) {
        queryInput.prefWidthProperty().bind(pane.widthProperty().divide(metric));
    }

    public TextField getQueryInput() {
        return queryInput;
    }

    public void setQueryInput(TextField queryInput) {
        this.queryInput = queryInput;
    }


    public void setBtSearch(Button btSearch) {
        this.btSearch = btSearch;
    }

    public HBox getPane() {
        return pane;
    }
}
