package finalproject;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex; // this will contain a set of pairs (String, ArrayList of
															// Strings)
	public MyWebGraph internet;
	public XmlParser parser;
	private double DAMPING_FACTOR = 0.5;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url. For each new
	 * page seen, it updates the wordIndex, the web graph, and the set of visited
	 * vertices.
	 * 
	 * This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {

		int internetSize = internet.vertexList.size();
		HashMap<String, Boolean> alreadyVisited = new HashMap<>(); // to know what I visited
		ArrayList<String> urlQueue = new ArrayList<>();

		// starting
		urlQueue.add(url);

		// to make sure all links are updated
		while (!urlQueue.isEmpty()) {
			String tempUrl = urlQueue.remove(0);

			if (!alreadyVisited.containsKey(tempUrl)) {
				alreadyVisited.put(tempUrl, true);
				internet.addVertex(tempUrl);
				ArrayList<String> links = parser.getLinks(tempUrl);
				int linksSize = links.size();
				int i = 0;

				while(i < linksSize ) {
					// get link
					String link = links.get(i);
					internet.addVertex(link);
					internet.addEdge(tempUrl, link);
					// to stop infinite cycles
					if (!urlQueue.contains(link) && !alreadyVisited.containsKey(link)) {
						urlQueue.add(link);
					}
					i++;
				}

			}

			// update the word index
			ArrayList<String> pageWords = parser.getContent(tempUrl);
			int wordsSize = pageWords.size();
			int j = 0;
			while(j < wordsSize ) {
				// get word
				String currentWord = pageWords.get(j);
				// make it lower case to not have problems
				currentWord = currentWord.toLowerCase();

				// if the key doesn't exist, make it
				if (!wordIndex.containsKey(currentWord)) {
					wordIndex.put(currentWord, new ArrayList<>());
				}
				
				ArrayList<String> urls = wordIndex.get(currentWord);
				if (!urls.contains(tempUrl)) {
				    urls.add(tempUrl);
				}
				
				j++;
			}

		}
	}

	/*
	 * This computes the pageRanks for every vertex in the web graph. It will only
	 * be called after the graph has been constructed using crawlAndIndex(). To
	 * implement this method, refer to the algorithm described in the assignment
	 * pdf.
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		ArrayList<String> vertices = new ArrayList<>(internet.getVertices());
		int i = 0;
		int k = 0;
		int verticesSize = vertices.size();
		
		// set them up to 1 to start
		while(k < vertices.size()) {
			String currentVertex = vertices.get(k);
			internet.setPageRank(currentVertex, 1);
			k++;
		}

		// set them up
		ArrayList<Double> actualranks = computeRanks(vertices);
		ArrayList<Double> oldRanks;
		
		// update the vertices's rank
		while(i < vertices.size()) {
			String currentVertex = vertices.get(i);
			double currentRank = actualranks.get(i);
			internet.setPageRank(currentVertex, currentRank);
			i++;
		}

		boolean convergent = false;
		while (!convergent) {

			oldRanks = actualranks;
			actualranks = computeRanks(vertices);
			convergent = true;

			int j = 0;
			while (j < verticesSize) {
				if (Math.abs(actualranks.get(j) - oldRanks.get(j)) >= epsilon) {
					convergent = false;
					break;
				}
				j++;
			}
			
			int m = 0;
			// update the vertices's rank
			while(m < vertices.size()) {
				String currentVertex = vertices.get(m);
				double currentVank = actualranks.get(m);
				internet.setPageRank(currentVertex, currentVank);
				m++;
			}

		}
	}
	
	

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the
	 * web graph and returns an ArrayList<double> representing the newly computed
	 * ranks for those urls. Note that the double in the output list is matched to
	 * the url in the input list using their position in the list.
	 * 
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {

		int arraySize = vertices.size();
		ArrayList<Double> ranks = new ArrayList<Double>();
		int i = 0;
		int j = 0;
		
		//set them up
		while(i < arraySize ) {
			ranks.add(1.0);
			i++;
		}
		
		//explore the vertices
	    while( j < arraySize) {
	    	double tempRank = 0.0;
	    	String tempVertex = vertices.get(j);
	    	
	    	ArrayList<String> intoVertices = this.internet.getEdgesInto(tempVertex);
	    	int n = 0;
	    	int intoSize = intoVertices.size();
	    	// explore the vertices that goes into it.
	    	while(n < intoSize ) {
				// get intoVertice
				String intoVertice = intoVertices.get(n); 
	    		int LinksOut = this.internet.getOutDegree(intoVertice);
	    		double rankIn = this.internet.getPageRank(intoVertice);
	    		tempRank += rankIn/LinksOut;
	    		n++;
	    	}
	    	// change for the correct value
	    	int vertexIndex = vertices.indexOf(tempVertex);
            ranks.set(vertexIndex, (1-DAMPING_FACTOR) + DAMPING_FACTOR *(tempRank));
            j++;
	    }
			
		return ranks;
	}

	/*
	 * Returns a list of urls containing the query, ordered by rank Returns an empty
	 * list if no web site contains the query.
	 * 
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
		
		// case sensitive
		query = query.toLowerCase();
		
		// set them up
		ArrayList<String> urlsOrdered = wordIndex.get(query);
		HashMap<String, Double> RanksOfPage = new HashMap<>();
		// if the query is not found, returns an empty array
		if(urlsOrdered == null) {
			urlsOrdered = new ArrayList<>();
			return urlsOrdered;
		}
		
		// get the ranks 
		for (String currenUrl : urlsOrdered) { 
			Double currentRank = internet.getPageRank(currenUrl);
			RanksOfPage.put(currenUrl, currentRank);	
		}
		
		// sort them using mergesort
		urlsOrdered = Sorting.fastSort(RanksOfPage);

		return urlsOrdered;
	}
}
