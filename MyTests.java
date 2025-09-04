package finalproject;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MyTests {
    @Test
    @DisplayName("Adding first vertex to graph")
    public void addingVertexToGraph() {
        MyWebGraph g = new MyWebGraph();

        //Creating my vertices
        boolean added = g.addVertex("siteLOL");
        boolean contained = g.vertexList.containsKey("siteLOL");

        assertTrue(added && contained, "Check return value of addVertex or add conditions.");
    }

    @Test
    @DisplayName("Adding duplicate vertex to the graph")
    public void addDuplicateVertexToGraph() {
        MyWebGraph g = new MyWebGraph();

        //Creating my vertices
        g.addVertex("siteLOL");

        assertFalse(g.addVertex("siteLOL"), "Should return false for duplicates");
    }

    @Test
    @DisplayName("Adding edge between two existing vertices")
    public void normalAddEdge() {
        MyWebGraph g = new MyWebGraph();

        //Adding vertices
        g.addVertex("siteA");
        g.addVertex("siteB");
        g.addVertex("siteC");
        g.addVertex("siteD");

        boolean atob = g.addEdge("siteA", "siteB");
        boolean atod = g.addEdge("siteA", "siteD");
        boolean btoa = g.addEdge("siteB", "siteA");
        boolean btoc = g.addEdge("siteB", "siteC");

        assertTrue(atob && atod && btoa && btoc, "Should all return true");
    }

    @Test
    @DisplayName("Adding duplicate edges")
    public void addDuplicateEdges() {
        MyWebGraph g = new MyWebGraph();

        //Adding vertices
        g.addVertex("siteA");
        g.addVertex("siteB");

        g.addEdge("siteA", "siteB");
        boolean duplicate = g.addEdge("siteA", "siteB");

        assertFalse(duplicate, "Trying to add duplicate edge should return false");
    }

    @Test
    @DisplayName("Adding edge but one of the vertices doesn't exist")
    public void addEdgeButSingleMissingVertex() {
        MyWebGraph g = new MyWebGraph();

        //Adding vertices
        g.addVertex("siteA");

        boolean bad = g.addEdge("siteA", "siteB");

        assertFalse(bad, "Should return false if at least one edge doesn't exist");
    }

    @Test
    @DisplayName("Adding edge between two non existent vertices")
    public void addEdgeButNoVertices() {
        MyWebGraph g = new MyWebGraph();

        boolean bad = g.addEdge("siteA", "siteB");

        assertFalse(bad, "Should return false if at least one edge doesn't exist");
    }

    @Test
    @DisplayName("Getting vertices that point to current vertex")
    public void normalGetEdgesInto() {
        MyWebGraph g = new MyWebGraph();

        //Adding vertices
        g.addVertex("siteA");
        g.addVertex("siteB");
        g.addVertex("siteC");
        g.addVertex("siteD");

        boolean atob = g.addEdge("siteA", "siteB");
        boolean btoa = g.addEdge("siteB", "siteA");
        boolean ctoa = g.addEdge("siteC", "siteA");
        boolean dtoa = g.addEdge("siteD", "siteA");

        ArrayList<String> inDegree = g.getEdgesInto("siteA");

        assertTrue(inDegree.contains("siteB") && inDegree.contains("siteC") && inDegree.contains("siteD"), "Check how you're adding your edges");
    }

    @Test
    @DisplayName("getEdgesInto when inDegree is 0")
    public void edgesIntoWhenInDegreeZero() {
        MyWebGraph g = new MyWebGraph();

        //Adding vertices
        g.addVertex("siteA");
        g.addVertex("siteB");

        boolean atob = g.addEdge("siteA", "siteB");

        ArrayList<String> inDegree = g.getEdgesInto("siteA");

        assertEquals(0, inDegree.size(), "Should be 0 since no edges into the vertex");
    }


    @Test
    @DisplayName("Fast sort with simple hashMap")
    public void fastSorting() {
        //Creating hashmap
        HashMap<Character, Integer> hash = new HashMap<>();
        hash.put('C', 1);
        hash.put('E', 2);
        hash.put('A', 3);
        hash.put('F', 4);
        hash.put('B', 5);
        hash.put('D', 6);

        ArrayList<Character> sorted = Sorting.fastSort(hash);

        assertEquals("[D, B, F, A, E, C]", sorted.toString());
    }

    @Test
    @DisplayName("Slow sort with simple hashMap")
    public void slowSorting() {
        //Creating hashmap
        HashMap<Character, Integer> hash = new HashMap<>();
        hash.put('C', 1);
        hash.put('E', 2);
        hash.put('A', 3);
        hash.put('F', 4);
        hash.put('B', 5);
        hash.put('D', 6);

        ArrayList<Character> sorted = Sorting.slowSort(hash);

        assertEquals("[D, B, F, A, E, C]", sorted.toString());
    }

    @Test
    @DisplayName("Crawl and Index with single vertex")
    public void crawlWithSingleVertex() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating search engine
            se = new SearchEngine("singleVert.xml");

            //Marking my single url
            String[] urls = {"siteA"};

            //Crawling over url
            se.crawlAndIndex("siteA");

            //Initializing arrays for proper comparison later on
            String expectedGraph = "[siteA]";

            String[] wordsArr = {"lol", "this", "project", "is", "not", "that", "bad"};
            ArrayList<String> words = new ArrayList<>(se.wordIndex.keySet());

            String expectedLinkToWord = "[siteA]";

            //Checking to see if we got all vertices
            assertEquals(expectedGraph, se.internet.getVertices().toString(), "Check if you're adding vertices correctly");

            //Checking if word index contains all words (my method only works if you put everything to lowercase in word index)
            boolean wordNotThere = false;

            for (String word : wordsArr) {
                if (!words.contains(word)) {
                    wordNotThere = true;
                    break;
                }
            }

            assertFalse(wordNotThere, "Check if words are added correctly to index");

            //Checking one word to see if it links to the proper url
            assertEquals(expectedLinkToWord, se.wordIndex.get("lol").toString(), "Check if you're adding correct URL's");

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }

    @Test
    @DisplayName("Crawl with single vertex that contains multiples of same word")
    public void crawlWithRepeatingWords() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating search engine
            se = new SearchEngine("repeatingWords.xml");

            //Below are mu urls
            String[] urls = {"siteA", "siteB"};

            //Crawling over A, since it is connected to B it should also go over B
            se.crawlAndIndex("siteA");

            ArrayList<String> keys = se.internet.getVertices();

            boolean missingKey = false;
            //Checking if urls were added to graph
            for(String key : urls) {
                if (!keys.contains(key)) {
                    missingKey = true;
                    break;
                }
            }

            assertFalse(missingKey, "Your crawlAndIndex method should crawl to all connected hyperlinks");

            //Initializing arrays for proper comparison later on
            String wordsArr = "[hey]";
            ArrayList<String> words = new ArrayList<>(se.wordIndex.keySet());

            //Checking if word was added
            assertEquals(wordsArr, words.toString(), "Word index should not be case specific");


            //Getting links to single word
            ArrayList<String> wordLinks = new ArrayList<>(se.wordIndex.get("hey"));

            //Checking if links to word matches
            boolean a = wordLinks.contains("siteA");
            boolean b = wordLinks.contains("siteB");

            assertTrue(a && b, "Checking if you're adding all proper links to the words");

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }

    @Test
    @DisplayName("PDF example for ranking and all")
    public void pdfExampleLastPart() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating new SearchEngine object
            se = new SearchEngine("pdfExample.xml");

            String[] urls = {"siteA", "siteB", "siteC", "siteD"};

            for(String url : urls) {
                se.crawlAndIndex(url);
            }

            se.assignPageRanks(0.01);

            boolean a = Math.abs(se.internet.getPageRank("siteA") - se.parser.getPageRank("siteA")) < 0.001;
            boolean b = Math.abs(se.internet.getPageRank("siteB") - se.parser.getPageRank("siteB")) < 0.001;
            boolean c = Math.abs(se.internet.getPageRank("siteC") - se.parser.getPageRank("siteC")) < 0.001;
            boolean d = Math.abs(se.internet.getPageRank("siteD") - se.parser.getPageRank("siteD")) < 0.001;

            assertTrue(a && b && c && d, "Something doesn't work oops");

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }

    @Test
    @DisplayName("Similar to previous but with ranking")
    public void pdfExampleButWithRanking() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating new SearchEngine object
            se = new SearchEngine("pdfWithRanking.xml");

            String[] urls = {"siteA", "siteB", "siteC", "siteD"};

            for(String url : urls) {
                se.crawlAndIndex(url);
            }

            se.assignPageRanks(0.01);

            String sorted = "[siteC, siteA, siteB, siteD]";

            assertEquals(sorted, se.getResults("ever").toString());

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }

    //You need to change the expected rank value of www.unreal.com to 0.718 because actual rank is like 0.7180555 or something like that
    @Test
    @DisplayName("Compute rankings of provided 'test' file")
    public void rankingsOfProvidedTestFile() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating new SearchEngine object
            se = new SearchEngine("test.xml");

            String[] urls = {"www.cs.mcgill.ca", "www.ea.com", "www.ubisoft.com", "www.eidos.com", "www.unreal.com", "www.unity.com"};

            for(String url : urls) {
                se.crawlAndIndex(url);
            }

            se.assignPageRanks(0.01);

            boolean a = Math.abs(se.internet.getPageRank("www.cs.mcgill.ca") - se.parser.getPageRank("www.cs.mcgill.ca")) < 0.001;
            boolean b = Math.abs(se.internet.getPageRank("www.ea.com") - se.parser.getPageRank("www.ea.com")) < 0.001;
            boolean c = Math.abs(se.internet.getPageRank("www.ubisoft.com") - se.parser.getPageRank("www.ubisoft.com")) < 0.001;
            boolean d = Math.abs(se.internet.getPageRank("www.eidos.com") - se.parser.getPageRank("www.eidos.com")) < 0.001;
            boolean e = Math.abs(se.internet.getPageRank("www.unreal.com") - se.parser.getPageRank("www.unreal.com")) < 0.001;
            boolean f = Math.abs(se.internet.getPageRank("www.unity.com") - se.parser.getPageRank("www.unity.com")) < 0.001;

            assertTrue(a && b && c && d && e && f, "Something doesn't work oops");

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }

    @Test
    @DisplayName("Compute rankings of provided 'testAcyclic' file")
    public void rankingsWithProvidedTestAcyclic() {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating new SearchEngine object
            se = new SearchEngine("testAcyclic.xml");

            String[] urls = {"siteA", "siteC", "siteD", "siteE"};

            for(String url : urls) {
                se.crawlAndIndex(url);
            }

            se.assignPageRanks(0.01);

            boolean a = Math.abs(se.internet.getPageRank("siteA") - se.parser.getPageRank("siteA")) < 0.001;
            boolean c = Math.abs(se.internet.getPageRank("siteC") - se.parser.getPageRank("siteC")) < 0.001;
            boolean d = Math.abs(se.internet.getPageRank("siteD") - se.parser.getPageRank("siteD")) < 0.001;
            boolean e = Math.abs(se.internet.getPageRank("siteE") - se.parser.getPageRank("siteE")) < 0.001;

            assertTrue(a && c && d && e, "Something doesn't work oops");

        } catch (Exception e) {
            fail("Check for exceptions in the previous steps");
        }
    }
}
