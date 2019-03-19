package wintec.itb.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import wintec.itb.core.InvertedIndex;
import wintec.itb.service.AppService;
import wintec.itb.service.AppStore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This code snippet is responsible for assembling all the
 * components for the UI, defining the event handler as well as
 * searching the database if needed.
 */
public class AppGUI extends Pane {

    private TextArea taResult;
    private AppStore appStore;
    private AppService appService = AppService.getInstance();

    public AppGUI() {
        this.appStore = AppStore.getInstance();
        this.appStore.invertedIndex = new InvertedIndex();

        // assembling each part of the UI
        assembling();

        // never block the main thread from listening the event from users
        new Thread(() -> {
            try {
                appService.connectingDB();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assembling() {
        //pane to hold everything
        BorderPane mainPane = new BorderPane();

        // set the center area
        taResult = new TextArea();
        taResult.setEditable(false);
        taResult.prefWidthProperty().bind(mainPane.widthProperty().subtract(50));
        taResult.prefHeightProperty().bind(mainPane.heightProperty().subtract(25));
        appStore.taResult = taResult;

        // set the top area
        TopHolder topHolder = new TopHolder();
        topHolder.bindWidthProperty(this, 1.5);
        Button btSearch = topHolder.getBtSearch();
        TextField queryInput = topHolder.getQueryInput();
        appStore.queryInput = queryInput;
        btSearch.setOnAction(event -> {
            try {
                appService.handleSearch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        HBox topArea = topHolder.getPane();

        // set the bottom area
        BottomHolder bottomHolder = new BottomHolder();
        bottomHolder.bindWithProperty(this, 1.5);
        appStore.lblStatus = bottomHolder.getLblStatus();
        bottomHolder.getBtBrowser().setOnAction(event -> appService.handleBrowse());
        HBox bottomArea = bottomHolder.getPane();

        // set the right area
        RightSideHolder rightSideHolder = new RightSideHolder();
        rightSideHolder.getBtUpdate().setOnAction(event -> {
            try {
                appService.handleUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        VBox rightArea =rightSideHolder.getPane();


        mainPane.setCenter(taResult);
        mainPane.setTop(topArea);
        mainPane.setBottom(bottomArea);
        mainPane.setRight(rightArea);

        // bind width and height
        mainPane.prefWidthProperty().bind(this.widthProperty());
        mainPane.prefHeightProperty().bind(this.heightProperty());

        this.getChildren().add(mainPane);
    }
}
