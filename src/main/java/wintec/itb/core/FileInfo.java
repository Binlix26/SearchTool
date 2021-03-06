package wintec.itb.core;

import java.util.ArrayList;

/**
 * Created by binlix26 on 25/03/17.
 *
 * The exact node on the posting list held by the Frequency Object.
 */
public class FileInfo {
    private int fileID;
    private int occurrence; // frequency in a file
    private ArrayList<Integer> positions = new ArrayList<>();

    public FileInfo(int fileID) {
        this.fileID = fileID;
        this.occurrence = 0;
    }

    public int getFileID() {
        return fileID;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public void addPosition(int pos) {
        positions.add(pos);
        this.occurrence++;
    }

}
