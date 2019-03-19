package wintec.itb.service;

import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import wintec.itb.database.DatabaseConnector;
import wintec.itb.utility.WordNormalization;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Manage browse, search and update event happening
 * throughout the application
 */
// TODO can be extract to a DatabaseService
public class AppService {
    // Singleton to make sure only one instance is active
    private static final AppService APP_SERVICE = new AppService();
    // DB connector
    private Connection conn;
    private AppStore appStore;

    private AppService() {
        this.appStore = AppStore.getInstance();
    }

    public void connectingDB() throws SQLException, ClassNotFoundException {
        conn = new DatabaseConnector().initDB();
    }

    public static AppService getInstance() {
        return APP_SERVICE;
    }

    public void handleBrowse() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(null);

        if (dir != null) {
            String status = dir.getAbsolutePath();

            // never affects the main Thread responding the UI event
            new Thread(() -> {
                // make sure the inverted index only contains the file under the current directory
                // clear the records for previously chosen directory
                if (!appStore.invertedIndex.isEmpty()) {
                    appStore.invertedIndex.reset();
                }

                // get inverted index for the files under the chosen directory
                appStore.invertedIndex.getFilesForInvertedIndex(dir);

                Platform.runLater(() -> appStore.lblStatus.setText(status));

                appStore.invertedIndex.displayInvertedIndex();
                appStore.invertedIndex.displayFileMap();
            }).start();
        }
    }

    public void handleSearch() throws SQLException {
        // NullPointerException will be thrown without this check
        if (appStore.invertedIndex == null) {
            appStore.taResult.setText("Please choose a directory before you search!");
            return;
        }

        // get text from user
        String queryList = appStore.queryInput.getText().trim();

        if (queryList.equals("")) {
            appStore.taResult.setText("Please enter your query before searching.");
            return;
        }
        // to hold the fileIDs that contain all the terms in the query
        ArrayList<int[]> idList = new ArrayList<>();

        //refresh the map: fileId = words ( the map is for better displaying results )
        appStore.invertedIndex.refreshIndexToWords();

        // prepare for searching the database
        PreparedStatement statement = null;
        if (appStore.isSynonymEnable) {
            String sql = "SELECT * FROM Words WHERE word = ?";
            statement = conn.prepareStatement(sql);
            System.out.println("isSynonymEnable is: " + appStore.isSynonymEnable);
        }

        // deal with the term list
        for (String word :
                queryList.split("\\W+")) {

            // stemming the word before it is searched as a key in the map
            String term = WordNormalization.normalize(word.toLowerCase());

            int[] arrOfIDs = null; // file IDs contain this term

            // search synonym database if necessary
            if (appStore.isSynonymEnable) {

                statement.setString(1, term);
                ResultSet resultSet = statement.executeQuery();
                System.out.println("<----- Querying ----->");

                if (resultSet.next()) {
                    String key = resultSet.getString(1);
                    String value = resultSet.getString(2);
                    String synonyms = key + "," + value;

                    System.out.println("Data from the database: ");
                    System.out.println(key + " -> " + value + "\n");

                    arrOfIDs = appStore.invertedIndex.getSynonymFilesID(synonyms);

                } else {
                    // some of the words do not exist as primary key in the database
                    // so search files for the term no matter what
                    System.out.println("There is NO DATA back!");
                    System.out.println(term + " is not registered in the database." + "\n");

                    arrOfIDs = appStore.invertedIndex.getArrayOfFileIDs(term);
                }

            } else {
                // don't need the database
                // put each array of fileIDs for each term in the ArrayList
                arrOfIDs = appStore.invertedIndex.getArrayOfFileIDs(term);
            }

            // check if there is any word not in the files
            if (arrOfIDs != null) {
                idList.add(arrOfIDs);
            } else {
                appStore.queryInput.setText("No match has been found!");
                return;
            }
        }

        // when user is only entering one word
        if (idList.size() == 1) {
            // pass the list and show the result
            ArrayList<Integer> listIntersection = new ArrayList<>();
            int[] temp = idList.get(0);
            for (int i = 0; i < temp.length; i++) {
                listIntersection.add(temp[i]);
            }

            showResult(listIntersection);
            return;
        }

        // intersections of the fileID need to be found!
        ArrayList<Integer> intersection = getFileIntersection(idList);

        if (intersection.size() != 0) {
            showResult(intersection);
        } else {
            appStore.taResult.setText("No match has been found!");
        }
    }

    public void handleUpdate() throws SQLException {

        String selectSQL = "SELECT * FROM Words WHERE word = ?";
        String updateSQL = "UPDATE Words SET Synonyms = ? WHERE word = " + "'" + appStore.keyword + "'";
        String insertSQL = "INSERT INTO Words (Word,Synonyms) VALUES (?,?)";

        PreparedStatement queryStat = conn.prepareStatement(selectSQL);
        queryStat.setString(1, appStore.keyword);
        queryStat.executeQuery();
        ResultSet resultSet = queryStat.getResultSet();

        // for later inserting or updating
        PreparedStatement statement;

        if (resultSet.next()) {
            statement = conn.prepareStatement(updateSQL);

//            String keyFromDB = resultSet.getString(1);
            String synonymsFromDB = resultSet.getString(2);

            System.out.println("Result from Database ====> ");
            System.out.println(appStore.keyword + " -> " + synonymsFromDB);
            System.out.println("<----- Updating ------>");
            System.out.println("The word to be added: " + appStore.synonym);

            // do not have to check repetitiveness by doing this
            Set<String> tempSynonyms = new HashSet<>(Arrays.asList(synonymsFromDB.split("\\W+")));
            // add user input
            tempSynonyms.add(appStore.synonym);

            StringBuilder stringBuilder = new StringBuilder();

            for (String target :
                    tempSynonyms) {
                stringBuilder.append(target).append(",");
            }

            int lastIndex = stringBuilder.length();
            stringBuilder.delete(lastIndex - 1, lastIndex);

            statement.setString(1, stringBuilder.toString());
            statement.executeUpdate();

            System.out.println(appStore.keyword + " -> " + stringBuilder.toString() + "\n");

        } else {
            System.out.println(appStore.keyword + " is not registered to the Database!");
            System.out.println("<------ Doing Inserting ----->");
            statement = conn.prepareStatement(insertSQL);
            statement.setString(1, appStore.keyword);
            statement.setString(2, appStore.synonym);
            statement.executeUpdate();
            System.out.println(appStore.keyword + " -> " + appStore.synonym + "\n");
        }
    }

    // TODO the folloing three methonds need to be organised
    private ArrayList<Integer> getFileIntersection(ArrayList<int[]> list) {
        ArrayList<Integer> intersection = new ArrayList<>();

        int[] temp = list.get(0);
        for (int i = 0; i < temp.length; i++) {
            intersection.add(temp[i]);
        }

        for (int i = 1; i < list.size(); i++) {
            intersection = getIntersectionBetweenTwo(intersection, list.get(i));
        }

        return intersection;
    }

    // only will be invoked by getFileIntersection
    private ArrayList<Integer> getIntersectionBetweenTwo(ArrayList<Integer> list, int[] arr) {
        ArrayList<Integer> target = new ArrayList<>();
        for (Integer iNum :
                list) {
            for (int i = 0; i < arr.length; i++) {
                if (iNum == arr[i])
                    target.add(iNum);
            }
        }
        return target;
    }

    private void showResult(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The list of results is: ").append("\n");
        for (Integer fileID :
                list) {
            stringBuilder.append(appStore.invertedIndex.getFilePathById(fileID))
                    .append("\n")
                    .append("Contains -> ")
                    .append(appStore.invertedIndex.getDetailedResult(fileID))
                    .append("\n");
        }

        appStore.taResult.setText(stringBuilder.toString());
    }
}
