package com.krishna.androidproj.goldcomp;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	/**
	 * @param args
	 */
	String sourceName;
	

	public double getResponse(URL url) throws IOException {
				
		 Document doc = Jsoup.connect(url.toString()).get();
		 String tex = "0";
		 if (sourceName.equalsIgnoreCase("hdfc"))
		 {
			 
		 Elements el = doc.select("td:containsOwn(Singapore Dollars)");
		 Element finalele = el.first().parent().parent().parent().parent().parent().nextElementSibling();
		 Element finalele2 = finalele.select("td:containsOwn(500)").first().nextElementSibling();
		  tex = finalele2.text();
		 
		 } else if (sourceName.equalsIgnoreCase("uob"))
		 {
			 Elements el = doc.select("b:containsOwn(PAMP GOLD BARS)");
			 Element finalele = el.first().parent().parent();
			 Element finalele2 = finalele.child(3);
			  tex = finalele2.text();
		 }
		 else if (sourceName.equalsIgnoreCase("hyd"))
		 {
			 Elements el = doc.select("td:containsOwn(Morning)");
			 Element finalele = el.first().parent().parent();
			 Element finalele2 = finalele.child(2);
			 if (finalele2.child(4).text().equalsIgnoreCase(""))
			  tex = finalele2.child(2).text().substring(3);
			 else
			  tex = finalele2.child(4).text().substring(3);
		 }
		return Double.parseDouble(tex);
		
	    }
	
	
	
	public Parser(String sourcename)
	{
		sourceName = sourcename;
	}
	
	

	
	//destructor
	public void close()
	{
		sourceName = "";
	}
	
	

}
