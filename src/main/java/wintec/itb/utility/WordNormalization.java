package wintec.itb.utility;

/**
 *
 * Provide the normalize()
 * function for everyone who will be invoking it to conduct a word normalization.
 */
public class WordNormalization {
    private static Stemmer stemmer = new Stemmer();

    private WordNormalization() {

    }

    // use the Porter stemmer Class to process the term
    public static String normalize(String term) {

        char[] chs = term.toCharArray();
        stemmer.add(chs, chs.length);
        stemmer.stem();

        String termRoot;
        termRoot = new String(
                stemmer.getResultBuffer(), 0, stemmer.getResultLength());

        return termRoot;

    }
}
