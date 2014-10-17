//Author: Hardik Shah
//Date: 10/15/2014

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

public class mypagerank {
	ArrayList<String> pages = new ArrayList<String>();
	ArrayList<String> sinknodes = new ArrayList<String>();
	HashMap<String, Double> outlinks = new HashMap<String, Double>();
	static HashMap<String, ArrayList<String>> inlinks = new HashMap<String, ArrayList<String>>();
	static HashMap<String, Double> pagerank = new HashMap<String, Double>();
	static HashMap<String, Double> inlinks_count = new HashMap<String, Double>();
	ArrayList<Double> perplexvalues = new ArrayList<Double>();
	double dampfactor = 0.85;
	private int count = 0;
	double perplexity = 0;


	public static void main(String args[]) throws IOException {
		mypagerank pr = new mypagerank();
		pr.readfile(args[0]);
		pr.pagerank();
		System.out.println("Top 50 pages by pagerank count: ");
		pr.sortByValues(pagerank);
		System.out.println("Top 50 pages by inlinks count: ");
		pr.sortByValues(inlinks_count);
	}

// Algorithm to calculate pagerank. Runs until perplexity values converge
	private void pagerank() {
		HashMap<String, Double> newpagerank = new HashMap<String, Double>();
		double plen = pages.size();
		double sinkpagerank = 0;
		double H = 0;
		// Assign Initial pagerank values to all pages
		for (String page : pages) {
			pagerank.put(page, (1.0 / plen));
			H += (pagerank.get(page) * (Math.log(pagerank.get(page)) / Math
					.log(2)));
		}
		perplexity = Math.pow(2, -H);
		System.out.println("Perplexity values: ");
		System.out.println("After 1 iteration: " + perplexity);
		count = 2;
		//int i = 0;
		while (!isconverged(perplexity)) {
			//i++;
			sinkpagerank = 0;
			for (String page : sinknodes)
				sinkpagerank += pagerank.get(page);	//Calculate total Sink pagerank
			for (String page : pages) {
				newpagerank.put(page, ((1 - dampfactor) / plen));	// Teleportation
				newpagerank.put(page, newpagerank.get(page)		// Spread remaining sink pagerank evenly 
						+ (dampfactor * sinkpagerank / plen));
				for (String q : inlinks.get(page)) {			
					newpagerank.put(page, newpagerank.get(page)	// Add share of pagerank from in-links
							+ (dampfactor * pagerank.get(q) / outlinks.get(q)));
				}
			}
			H = 0;
			for (String page : pages) {
				pagerank.put(page, newpagerank.get(page));
				H += (pagerank.get(page) * (Math.log(pagerank.get(page)) / Math
						.log(2)));
			}
			perplexity = Math.pow(2, -H);
			System.out.println("After " + count + " iterations: " + perplexity);
			count++;
			/*for (String page : pagerank.keySet()) {
				System.out.println("Page: " + page + " Pagerank: "
						+ pagerank.get(page));
			}*/
		}
	}

	private boolean isconverged(double perplexity2) {
		if (perplexvalues.size() > 5)
			perplexvalues.remove(0);
		perplexvalues.add(perplexity2);
		if (perplexvalues.size() < 5)
			return false;
		else {
			// Check for difference between last 4 perplexity values
			if (Math.abs(perplexvalues.get(0) - perplexvalues.get(1)) < 1
					&& Math.abs(perplexvalues.get(1) - perplexvalues.get(2)) < 1
					&& Math.abs(perplexvalues.get(2) - perplexvalues.get(3)) < 1
					&& Math.abs(perplexvalues.get(3) - perplexvalues.get(4)) < 1) {
				return true;
			}
			perplexvalues.remove(0);
			return false;
		}
	}

	private void readfile(String file) throws IOException {
		InputStream is = mypagerank.class.getResourceAsStream(file);
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		String line = null;
		String[] temp = null;
		String p = null;
		System.out.println("Starting to read file and set variables..");
		while ((line = br.readLine()) != null) {
			// Read file and Store the page and inlink page as key:value in HashMap
			ArrayList<String> templist = new ArrayList<String>();
			line = line.trim();
			temp = line.split("\\s+");
			p = temp[0];
			pages.add(p);
			for (int i = 1; i < temp.length; i++) {
				if (!templist.contains(temp[i]))
					templist.add(temp[i]);
			}
			inlinks.put(p, templist);
		}
		br.close();
		checklinks();
		// Populate count of in-links
		for (String key : inlinks.keySet()) {
			double link_count = inlinks.get(key).size();
			inlinks_count.put(key, link_count);
		}
		System.out.println("Completed setting variables..");
	}

	private void checklinks() {
		// Iterate over inlinks to populate the outlinks count of each page
		for (String key : inlinks.keySet()) {
			for (String value : inlinks.get(key)) {
				if (outlinks.keySet().contains(value)) {
					outlinks.put(value, outlinks.get(value) + 1);
				} else {
					outlinks.put(value, 1.0);
				}
			}
		}
		// Populate the sink nodes Arraylist by iterating over nodes that have no outlinks.
		for (String p : pages) {
			if (!(outlinks.keySet().contains(p))) {
				if (!sinknodes.contains(p)) {
					sinknodes.add(p);
				}
			}
		}
	}
	// This method sorts the pagerank and inlinks HashMap.(HashMap are always unsorted)
	private void sortByValues(HashMap<String, Double> map) {
		Set<Entry<String, Double>> set = map.entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(
				set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		int count = 0;
		for (Map.Entry<String, Double> entry : list) {
			System.out.println("Document ID: " + entry.getKey() + " Value: "
					+ entry.getValue());
			count++;
			if (count > 49)
				break;
		}
	}
}
