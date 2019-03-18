package wintec.itb;

import org.junit.Test;
import wintec.itb.core.InvertedIndex;

import java.io.File;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by binlix26 on 8/06/17.
 */
public class InvertedIndexTest {
    private InvertedIndex invertedIndex = new InvertedIndex();
    private String path = "/Users/binlix26/Documents/test search";

    @Test
    public void testGetInvertedIndex() {
        File dir = new File(path);
        invertedIndex.getFilesForInvertedIndex(dir);

        TreeMap<Integer, File> indexFiles = invertedIndex.getIndexToFiles();

        assertThat(indexFiles.size(), is(4));
    }

    @Test
    public void testGetSynonymFilesID() {
        File dir = new File(path);
        invertedIndex.getFilesForInvertedIndex(dir);

        invertedIndex.refreshIndexToWords();

        int[] result = invertedIndex.getSynonymFilesID("leap");

        assertThat(result.length, is(1));
        assertThat(result[0], is(3));
    }

    @Test
    public void testGetArrayOfFileIDs() {
        File dir = new File(path);
        invertedIndex.getFilesForInvertedIndex(dir);

        invertedIndex.refreshIndexToWords();

        int[] result = invertedIndex.getArrayOfFileIDs("jump");

        assertThat(result.length, is(1));
        assertThat(result[0], is(4));
    }

    @Test
    public void testGetFilePathById() {
        File dir = new File(path);
        invertedIndex.getFilesForInvertedIndex(dir);

        String path = invertedIndex.getIndexToFiles().get(1).getAbsolutePath();

        assertThat(path, is("/Users/binlix26/Documents/test search/doc1.txt"));
    }

    @Test
    public void testGetDetailedResult() {
        File dir = new File(path);
        invertedIndex.getFilesForInvertedIndex(dir);

        invertedIndex.refreshIndexToWords();

        invertedIndex.getArrayOfFileIDs("jump");

        String word = invertedIndex.getDetailedResult(4);

        assertThat(word, is("jump"));
    }
}
