package wintec.itb.ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import wintec.itb.service.AppStore;
import wintec.itb.utility.WordNormalization;

public class RightSideHolder {
    private VBox pane;
    private Button btUpdate;
    private RadioButton enableDB;
    private RadioButton disableDB;

    public RightSideHolder() {
        this.pane = new VBox(15);
        this.pane.setPadding(new Insets(5));
        this.pane.setStyle("-fx-border-color: green");
        this.pane.setPadding(new Insets(5));
        this.pane.setStyle("-fx-border-color: green");

        this.enableDB = new RadioButton("Enable");
        this.disableDB = new RadioButton("Disable");
        this.btUpdate = new Button("UpdateDB");

        pane.getChildren().addAll(new Label("Database: "), enableDB, disableDB, btUpdate);
        disableDB.setSelected(true);
        ToggleGroup group = new ToggleGroup();
        enableDB.setToggleGroup(group);
        disableDB.setToggleGroup(group);
        bindRadioEvent();
    }

    public VBox getPane() {
        return pane;
    }

    public Button getBtUpdate() {
        return btUpdate;
    }

    private void bindRadioEvent() {
        // event handle
        enableDB.setOnAction(event -> {
            if (enableDB.isSelected())
                AppStore.getInstance().isSynonymEnable = true;
            // debug purpose
            System.out.println("Inside the EventHandling, Enable DB is :" + AppStore.getInstance().isSynonymEnable + "\n");
        });

        disableDB.setOnAction(event -> {
            if (disableDB.isSelected())
                AppStore.getInstance().isSynonymEnable = false;
            // debug purpose
            System.out.println("Inside the EventHandling, Enable DB is :" + AppStore.getInstance().isSynonymEnable + "\n");
        });
    }

    // TODO fix update pop up
    private void popUp() {
        btUpdate.setOnAction(event -> {
            GridPane updatePane = new GridPane();

            updatePane.setAlignment(Pos.CENTER);
            updatePane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
            updatePane.setHgap(5.5);
            updatePane.setVgap(5.5);

            TextField tfKeyWord = new TextField();
            TextField tfSynonym = new TextField();

            updatePane.add(new Label("Key Word:"), 0, 0);
            updatePane.add(tfKeyWord, 1, 0);
            updatePane.add(new Label("Synonym:"), 0, 1);
            updatePane.add(tfSynonym, 1, 1);

            Button btAdd = new Button("Add Synonym");
            Label lblPrompt = new Label("please enter one synonym per time.");
            lblPrompt.setTextFill(Color.BLUE);
            lblPrompt.setStyle("-fx-font-family: sans-serif");

            updatePane.add(lblPrompt, 1, 3);
            updatePane.add(btAdd, 1, 4);
            GridPane.setHalignment(btAdd, HPos.RIGHT);

            btAdd.setOnAction(event1 -> {
                Boolean isSucceed = true;
                String keyWord = tfKeyWord.getText();
                String synonym = tfSynonym.getText();

                if (keyWord.equals("") || synonym.equals("")) {
                    lblPrompt.setText("Both fields must be filled out.");
                    return;
                }

                if ((keyWord.split("\\W+").length != 1) ||
                        (synonym.split("\\W+").length != 1)) {
                    lblPrompt.setText("One key word and One synonym per time.");
                    return;
                }

                keyWord = WordNormalization.normalize(keyWord);
                synonym = WordNormalization.normalize(synonym);

                // TODO event binding
//                try {
//                    handleUpdate(keyWord, synonym);
//                } catch (SQLException e) {
//                    isSucceed = false;
//                    e.printStackTrace();
//                }

                if (isSucceed) {
                    lblPrompt.setText("Update Successful!");
                    tfKeyWord.setText("");
                    tfSynonym.setText("");
                } else
                    lblPrompt.setText("Failed, Try again!");

            });

            Stage stage = new Stage();
            Scene scene = new Scene(updatePane, 450, 200);
            stage.setScene(scene);
            stage.setTitle("Update Database");
            stage.show();
        });

    }
}
