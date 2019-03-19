package wintec.itb.service;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import wintec.itb.core.InvertedIndex;

/**
 * Holder the necessary fields needed for search handler.
 * Can be extended in the future with ease
 *
 * Inspired by React app data flow, not sure if this is good practice.
 * TODO need to find out
 * */
public class AppStore {

    private final static AppStore APP_STORE = new AppStore();

    public InvertedIndex invertedIndex;

    //top
    public TextField queryInput;

    //centre
    public TextArea taResult;

    //bottom
    public Label lblStatus;

    //right
    public boolean isSynonymEnable;
    public String keyword;
    public String synonym;

    private AppStore() {
    }

    public static AppStore getInstance() {
        return APP_STORE;
    }
}
