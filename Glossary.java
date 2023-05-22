import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program to take in an input text file containing words and their
 * corresponding definitions and converts them all to a specific HTML file in
 * the form of a glossary.
 *
 * @author Jacob Covert
 *
 */
public final class Glossary {
    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
    }

    /**
     * Generates a queue containing all of the terms from a text file to be used
     * inside of the glossary.
     *
     * @param input
     *            source of terms
     * @return queue of terms from input
     * @requires input.is_open
     */
    private static Queue<String> getQueueTerms(SimpleReader input) {
        assert input != null : "Violation of: input is not null";
        assert input.isOpen() : "Violation of: input.is_open";

        Queue<String> terms = new Queue1L<>();
        /*
         * Adds line to queue if it is a single term
         */
        while (!input.atEOS()) {
            String line = input.nextLine();
            if (line.indexOf(" ") < 0 && !line.isEmpty()) {
                terms.enqueue(line);
            }
        }

        return terms;
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";
        /*
         * Adds character to set if it is not already included
         */
        String str1 = str;

        while (str1.length() > 0) {
            char x = str1.charAt(0);
            if (!strSet.contains(x)) {
                strSet.add(x);
            }
            str1 = str1.substring(1);
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        boolean isSeparator = false;
        String nextWS = "";
        /*
         * Checks if character at position is a separator
         */
        for (char x : separators) {
            if (text.charAt(position) == x) {
                isSeparator = true;
            }
        }

        nextWS += Character.toString(text.charAt(position));
        /*
         * Adds each character until a character is not a separator
         */
        if (isSeparator) {
            for (int i = position + 1; i < text.length(); i++) {
                boolean isSeparator1 = false;
                for (char x : separators) {
                    if (text.charAt(i) == x) {
                        isSeparator1 = true;
                    }
                }

                if (isSeparator1) {
                    nextWS += Character.toString(text.charAt(i));
                } else {
                    i = text.length();
                }
            }
            /*
             * Adds characters until a separator is reached
             */
        } else {
            for (int k = position + 1; k < text.length(); k++) {
                boolean isSeparator2 = false;
                for (char x : separators) {
                    if (text.charAt(k) == x) {
                        isSeparator2 = true;
                    }
                }

                if (!isSeparator2) {
                    nextWS += Character.toString(text.charAt(k));
                } else {
                    k = text.length();
                }
            }
        }

        return nextWS;
    }

    /**
     * Generates a map of all of the terms of the glossary and their
     * corresponding definitions.
     *
     * @param input
     *            source of terms and definitions
     * @return map with each term as a key and each definition as a value
     * @requires input.is_open
     */
    private static Map<String, String> getMapGlossary(SimpleReader input) {
        assert input != null : "Violation of: input is not null";
        assert input.isOpen() : "Violation of: input.is_open";

        Map<String, String> glossary = new Map1L<>();

        while (!input.atEOS()) {
            String word = "";
            String line = input.nextLine();
            /*
             * Checks that string is a term
             */
            if (line.indexOf(" ") < 0 && !line.isEmpty()) {
                word = line;
            }
            String definition = "";
            line = input.nextLine();
            /*
             * Adds each line to the definition, making sure to add a space
             * between lines, until an empty string is reached
             */
            while (!line.isEmpty()) {
                if (definition == "") {
                    definition += line;
                    line = input.nextLine();
                } else {
                    definition += " ";
                    definition += line;
                    line = input.nextLine();
                }
            }
            /*
             * Adds words and corresponding definitions to map
             */
            glossary.add(word, definition);
        }

        return glossary;
    }

    /**
     * Prints definition to specified file, making sure to link any terms which
     * are also inside of the glossary.
     *
     * @param term
     *            word of which the definition must be printed
     * @param definition
     *            string containing the definition of the word being looked at
     * @param terms
     *            queue containing every term inside of the glossary
     * @param feedOut
     *            html file to output definition text to
     * @requires the definition correlates to the term & feedOut.is_open
     */
    private static void printDefinition(String term, String definition,
            Queue<String> terms, SimpleWriter feedOut) {
        /*
         * Define separator characters for test
         */
        final String separatorStr = " \t, ";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);
        /*
         * Processes definition, printing one word or separator at a time
         */
        int position = 0;
        while (position < definition.length()) {
            String piece = nextWordOrSeparator(definition, position,
                    separatorSet);
            if (separatorSet.contains(piece.charAt(0))) {
                feedOut.print(piece);
            } else {
                boolean link = false;
                /*
                 * If piece is a word, it is compared to the terms in the
                 * glossary and a hyperlink is created if it matches any words
                 */
                for (String s : terms) {
                    if (piece.equals(s)) {
                        feedOut.println("<a href=\"" + piece + ".html\">"
                                + piece + "</a>");
                        feedOut.println();
                        link = true;
                    }
                }
                if (!link) {
                    feedOut.print(piece);
                }
            }
            position += piece.length();
        }

    }

    /**
     * Processes one term into its own HTML output file, including its
     * definition along with links to any necessary words.
     *
     * @param term
     *            word at which an html file containing the proper definition
     *            refers to
     * @param glossary
     *            map containing every word and its corresponding definitions
     *            inside of glossary
     * @param terms
     *            queue containing every term inside of glossary
     * @param folder
     *            specified folder to output files to
     * @requires queue and map both contain term & folder exists
     */
    private static void processTerm(String term, Map<String, String> glossary,
            Queue<String> terms, String folder) {
        /*
         * Writes term's html file to correct folder
         */
        SimpleWriter feedOut = new SimpleWriter1L(
                folder + "/" + term + ".html");
        /*
         * Generate's html file for term
         */
        feedOut.println("<html>");
        feedOut.println("<head>");
        feedOut.println("<title>" + term + "</title>");
        feedOut.println("</head>");
        feedOut.println("<body>");
        feedOut.println("<h2>");
        feedOut.println("<b>");
        feedOut.println("<i>");
        feedOut.println("<font color=\"red\">" + term + "</font>");
        feedOut.println("</i>");
        feedOut.println("</b>");
        feedOut.println("</h2>");
        feedOut.println("<blockquote>");
        feedOut.println();
        /*
         * Prints definition of term, making sure to hyperlink any terms already
         * in glossary
         */
        printDefinition(term, glossary.value(term), terms, feedOut);

        feedOut.println("</blockquote>");
        feedOut.println("<hr>");
        feedOut.println("<p>");
        feedOut.println("Return to ");
        feedOut.println("<a href=\"index.html\">index</a>");
        feedOut.print(".");
        feedOut.println("</p>");
        feedOut.println("</body>");
        feedOut.println("</html>");

        feedOut.close();
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Input file (with terms and definitions): ");
        String inputFileName = in.nextLine();
        /*
         * Get folder name
         */
        out.print("Input folder name to output html files to: ");
        String folder = in.nextLine();
        /*
         * Create two file readers, one being used to create queue and the other
         * to create map
         */
        SimpleReader inFile = new SimpleReader1L(inputFileName);
        SimpleReader inFile2 = new SimpleReader1L(inputFileName);
        /*
         * Creates queue from file terms
         */
        Queue<String> terms = getQueueTerms(inFile);
        /*
         * Creates map from file terms and definitions
         */
        Map<String, String> glossary = getMapGlossary(inFile2);
        /*
         * Close file inputs
         */
        inFile.close();
        inFile2.close();
        /*
         * Sort lines into non-decreasing lexicographic order
         */
        Comparator<String> order = new StringLT();
        terms.sort(order);
        /*
         * Writes html files to correct folder location as specified by user
         */
        SimpleWriter fileOut = new SimpleWriter1L(folder + "/index.html");
        /*
         * Creates index file
         */
        fileOut.println("<html>");
        fileOut.println("<head>");
        fileOut.println("<title>Glossary</title>");
        fileOut.println("</head>");
        fileOut.println("<body>");
        fileOut.println("<h2>Glossary</h2>");
        fileOut.println("<hr>");
        fileOut.println("<h3>Index</h3>");
        fileOut.println("<ul>");
        /*
         * Processes list of terms
         */
        for (String term : terms) {
            /*
             * Processes each individual term's html files
             */
            processTerm(term, glossary, terms, folder);

            fileOut.println("<li>");
            fileOut.println("<a href=\"" + term + ".html\">" + term + "</a>");
            fileOut.println("</li>");
        }
        fileOut.println("</ul>");
        fileOut.println("</body>");
        fileOut.println("</html>");

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
        fileOut.close();
    }

}
