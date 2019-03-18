package wintec.itb;

import org.junit.Test;
import wintec.itb.core.FileInfo;
import wintec.itb.core.Frequency;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by binlix26 on 9/06/17.
 */
public class PostingListTest {

    private Frequency frequency = new Frequency();

    @Test
    public void testAddPosition() {
        frequency.addPosition(1, 5);
        frequency.addPosition(2, 9);

        int fre = frequency.getFrequency();

        assertThat(fre, is(2));
    }

    @Test
    public void testGetFileIds() {
        frequency.addPosition(1, 5);
        frequency.addPosition(4, 9);

        int[] ids = frequency.getFileIDs();

        assertThat(ids.length, is(2));
        assertThat(ids[0], is(1));
        assertThat(ids[1], is(4));
    }

    @Test
    public void testGetPosting() {
        frequency.addPosition(1,5);
        frequency.addPosition(1,9);
        frequency.addPosition(3,11);

        List<FileInfo> posting = frequency.getPosting();

        assertThat(posting.size(), is(2));

        FileInfo firstNode = posting.get(0);
        FileInfo secondNode = posting.get(1);

        assertThat(firstNode.getFileID(), is(1));
        assertThat(firstNode.getOccurrence(), is(2));

        assertThat(secondNode.getFileID(), is(3));
        assertThat(secondNode.getOccurrence(), is(1));
    }

}
