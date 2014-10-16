// Author: Hardik Shah
// Date: 09/26/2014 
package webcrawler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Mywebcrawler {
	private static String doctext = "";
	private static String temp = "";
	static String w = "";
	static String tempvar = "";
	private static boolean pendingDepthIncrease;
	static String mykey = "";
	private static String seed ="";
	private static int counter1 = 0;

	public static void main(String[] args) throws SQLException, IOException {
		mykey = args[1];
		seed = args[0];
		if (!(seed.isEmpty())) {
			processpages(seed, mykey.toLowerCase());
		} else {
			System.out.println("Invalid input. provide seed page");
		}
	}

	public static String removehash(String url) {
		temp = "";
		for (int i = 0; i < url.length(); i++) {
			if (url.charAt(i) == '#') {
				break;
			} else {
				temp = temp + url.charAt(i);
			}
		}
		return temp;
	}

	public static void processpages(String rooturl, String keyphrase) {
		LinkedList<String> links = new LinkedList<String>();
		HashMap<String, Boolean> hasVisited = new HashMap<String, Boolean>();
		links.add(rooturl);
		hasVisited.put(rooturl, true);
		int currentDepth = 0, elementsToDepthIncrease = 1;
		while (links.size() > 0) {
			try {
				String nxt = links.poll();
				elementsToDepthIncrease--;
				Thread.sleep(1000);
				Document doc = Jsoup.connect(nxt).get();
				doctext = doc.text().toLowerCase();
				Elements questions = doc.select("a[href]");
				if (elementsToDepthIncrease == 0) {
					currentDepth++;
					pendingDepthIncrease = true;
				}
				if (currentDepth > 3)
					return;
				if (!keyphrase.equals("")) {
					if (doctext.contains(keyphrase)) {
						counter1++;
						System.out.println(nxt);
						if (currentDepth < 3) {
							for (Element link : questions) {
								if (link.attr("abs:href").startsWith(
										"http://en.wikipedia.org/wiki/", 0)
										&& !(link.attr("abs:href")
												.startsWith(
														"http://en.wikipedia.org/wiki/Main_Page",
														0))) {
									w = removehash(link.attr("abs:href")
											.toString());
									Document d = Jsoup.connect(w).get();
									tempvar = d.select("link[rel=canonical]")
											.attr("href");
									if (!tempvar.equals(""))
										w = tempvar;
									if (hasVisited.get(w) == null) {
										if (!w.substring(6).contains(":")) {
											links.add(w);
											hasVisited.put(w, true);
											if (pendingDepthIncrease) {
												pendingDepthIncrease = false;
												elementsToDepthIncrease = links
														.size();
											}
										}
									}
								}
							}

						} else {
							if (pendingDepthIncrease) {
								pendingDepthIncrease = false;
								elementsToDepthIncrease = links.size();
							}
						}
					}
				} else if (keyphrase.equals("")) {
					System.out.println(nxt);
					if (currentDepth < 3) {
						for (Element link : questions) {
							if (link.attr("abs:href").startsWith(
									"http://en.wikipedia.org/wiki/", 0)
									&& !(link.attr("abs:href")
											.startsWith(
													"http://en.wikipedia.org/wiki/Main_Page",
													0))) {
								w = removehash(link.attr("abs:href").toString());
								Document d = Jsoup.connect(w).get();
								tempvar = d.select("link[rel=canonical]").attr(
										"href");
								if (!tempvar.equals(""))
									w = tempvar;
								if (hasVisited.get(w) == null) {
									if (!w.substring(6).contains(":")) {
										links.add(w);
										hasVisited.put(w, true);
										counter1++;
										if (pendingDepthIncrease) {
											pendingDepthIncrease = false;
											elementsToDepthIncrease = links
													.size();
										}
									}
								}
							}
						}
					} else {
						if (pendingDepthIncrease) {
							pendingDepthIncrease = false;
							elementsToDepthIncrease = links.size();
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}
}
