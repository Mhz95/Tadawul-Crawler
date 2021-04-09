import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringEscapeUtils;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;

/**
 * Assignment 1: Crawler
 * Selected Topics in AI, KSU 2021
 * @authors Dr.malsulmi, malzaidan
 */
public class TadawulCrawler {

	public static void main(String[] args) {
		String[] stockCode = { "1820", "4050", "3010"};

		crawl(stockCode[0]);
		specialXMLformat(stockCode[0]);

		crawl(stockCode[1]);
		specialXMLformat(stockCode[1]);

		crawl(stockCode[2]);
		specialXMLformat(stockCode[2]);
	}

	public static void crawl(String stockCode) {

		try {

			// use jsoup + SSLHelper to connect and obain the document from a link
			org.jsoup.nodes.Document doc = SSLHelper.getConnection(
					"https://www.tadawul.com.sa/wps/portal/tadawul/markets/Press-Release/news-%26-announcements/!ut/p/z1/pZDLjoJAEEW_xQXrvjag6I4BBAYkgww-emNajUjCKxPU6NfbPjYmimOsXSXn3EpdwsiUsILv0oTXaVnwTOwz1pkHjm840KhnW78y9I45NEaBSwGQyQWg1NDaPQU-_G5bADbccKjICGXC3vJtN-hCD3VnPBgLVKOf-VD-5-PJ6HjlfxOWZOXiWtWmrqu-BAk1X_H9NpOEvizziheH6JAvSgFRyOej7D4XDjVFruV5pqFSfKk3oKnXe-BBcY3AuZkL0PB6xP9IlcdxPD3668hNf5JW6wSWl2M9/dz/d5/L0lJSklLVUtVSklBIS9JTGpBQUF4QUFFUWtLa3JvQUdZIS80TmxHUW9ZaE9MZ1N1M01RL1o2X05ITENIMDgySzBURTAwQU1PSkZSNUoxOEwwL1o3X0lQRzQxSTgySzhOSzMwQUUxSzA0SE0xMDQ0L25vcm1hbC92aWV3/?annoucmentType=1_-1&searchType=1&symbol="
							+ stockCode)
					.userAgent("Chrome").get();

			// To find total number of announcements pages:
			org.jsoup.nodes.Element numPages_elm = doc.select("span.dot").first();
			int numPages = Integer.parseInt(numPages_elm.text().split("\\s+")[2]);

			// To build XML structure:
			Element root = new Element("DOCUMENTS");
			root.addContent(new Text("\n\n"));
			Document doca = new Document();

			doc = SSLHelper.getConnection(
					"https://www.tadawul.com.sa/wps/portal/tadawul/markets/Press-Release/news-%26-announcements/!ut/p/z1/pZJLc4IwFIV_iwuXndyEgLQ7BORNq-ADNk60VOnIYxR19Nc3oAttq3amd5ebc-658yUoRhMU52yXLliVFjlb8XMUS1PfdFUTZOJAqAMo3qvdG4g2ll1A40ZAiCrjZwouuB0MimSA1feoAH0BxZd-Qw8Ffq156sC3CMAPv2H5HVD6ijnqjbhUJv_zA73239ofbpQCf_PfyjeER34bxYtVMTuhXlZV-dKGNlTsne23qzaPnxdZyfJDcMhmBRcREOrQ-Ntck2ici-44mioS6Ipnwb13uRb8Av6uoCbbCO6hawTWm0GxxZeUQ6kLylB1fc0PMIjSgwgOL2BrFHHCnYspvsO_gI4doKaHgVI0zIt1xgGOd2myR0E9dZOw9XwZHsoERbhusDwvtvMsyatzc_rU9DcnrFGDtcyGvCZH9yOwUuuTRvbOFxet1hd2ITRB/p0/IZ7_IPG41I82K8NK30AE1K04HM1044=CZ6_NHLCH082K0TE00AMOJFR5J18L0=M/?searchType=1&datePeriod=-1&textSearch=&pageNo="
							+ 1 + "&annoucmentType=1_-1&productType=E&sectorId=-1&symbol=" + stockCode)
					.userAgent("Chrome").get();

			Elements links = doc.select("a[href]");

			// iterate over all pages, then extract the link to each announcement, access
			// the announcement page and you should take it from there
			for (int i = 1; i <= numPages; i++) {
				System.out.println("Page No : " + i + " out of " + numPages);
				doc = SSLHelper.getConnection(
						"https://www.tadawul.com.sa/wps/portal/tadawul/markets/Press-Release/news-%26-announcements/!ut/p/z1/pZJLc4IwFIV_iwuXndyEgLQ7BORNq-ADNk60VOnIYxR19Nc3oAttq3amd5ebc-658yUoRhMU52yXLliVFjlb8XMUS1PfdFUTZOJAqAMo3qvdG4g2ll1A40ZAiCrjZwouuB0MimSA1feoAH0BxZd-Qw8Ffq156sC3CMAPv2H5HVD6ijnqjbhUJv_zA73239ofbpQCf_PfyjeER34bxYtVMTuhXlZV-dKGNlTsne23qzaPnxdZyfJDcMhmBRcREOrQ-Ntck2ici-44mioS6Ipnwb13uRb8Av6uoCbbCO6hawTWm0GxxZeUQ6kLylB1fc0PMIjSgwgOL2BrFHHCnYspvsO_gI4doKaHgVI0zIt1xgGOd2myR0E9dZOw9XwZHsoERbhusDwvtvMsyatzc_rU9DcnrFGDtcyGvCZH9yOwUuuTRvbOFxet1hd2ITRB/p0/IZ7_IPG41I82K8NK30AE1K04HM1044=CZ6_NHLCH082K0TE00AMOJFR5J18L0=M/?searchType=1&datePeriod=-1&textSearch=&pageNo="
								+ i + "&annoucmentType=1_-1&productType=E&sectorId=-1&symbol=" + stockCode)
						.userAgent("Chrome").get();

				// To extract all links in this page
				links = doc.select("a[href]");

				for (org.jsoup.nodes.Element link : links) {

					// this will filter only links to announcements
					if (link.attr("abs:href").contains("/wps/portal/tadawul/home/announcement-details")) {
						String announLink = link.attr("abs:href");

						// To add newlines as in the assignment sample:
						Text[] newline = { new Text("\n\n"), new Text("\n\n"), new Text("\n\n"), new Text("\n\n"),
								new Text("\n\n"), new Text("\n\n"), new Text("\n\n"), new Text("\n\n") };

						// To structure XML:
						Element document = new Element("DOCUMENT");
						document.addContent(newline[0]);
						document.addContent(new Element("STOCKCODE").addContent(stockCode));
						document.addContent(newline[1]);

						// To get and parse timestamp:
						Elements timestamp = link.getElementsByClass("time_stamp");
						String tmp = timestamp.html();
						tmp = tmp.replace("&nbsp;", " ");

						String greg_date = getDate(tmp);
						String time_str = getTime(tmp);

						document.addContent(new Element("DATE").addContent(greg_date));
						document.addContent(newline[2]);
						document.addContent(new Element("TIME").addContent(time_str));
						document.addContent(newline[3]);
						document.addContent(new Element("LINK").addContent(announLink));
						document.addContent(newline[4]);

						// To move inside an announcement:
						doc = Jsoup.connect(announLink).timeout(300000).get();

						Elements title_elm = doc.select("h1");
						String title_str = title_elm.text();

						document.addContent(new Element("TITLE").addContent(title_str));
						document.addContent(newline[5]);

						// To parse p and table content:
						org.jsoup.nodes.Element table = doc.select("div.article_body").first();
						Elements table_p = table.select("p");
						Elements table_headings = table.select("th");
						Elements table_elms = table.select("td");

						String full_article = "";
						full_article += table_p.text();
						for (org.jsoup.nodes.Element th : table_headings) {
							full_article += th.text() + " ";
						}
						for (org.jsoup.nodes.Element td : table_elms) {
							full_article += td.text() + " ";
						}

						// Escape XML 1.0 by apache
						// To resolve encoding special chars i.e. 0x000b and 0x001a:
						full_article = StringEscapeUtils.escapeXml10(full_article);

						document.addContent(new Element("TEXT").addContent(full_article));
						document.addContent(newline[6]);

						root.addContent(document);
						root.addContent(newline[7]);

					}

				}

			}
			buildXmL(root, doca, stockCode);

		} catch (IOException ex) {
			Logger.getLogger(TadawulCrawler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void specialXMLformat(String file) {
		String filename = file + ".xml";
		int num = trim(filename, 1, 3);
		num = trim(filename, (num - 4), 2);
	}

	public static void buildXmL(Element root, Document doca, String stockCode) {

		doca.setContent(root);
		XMLOutputter outter = new XMLOutputter();
		outter.setFormat(Format.getRawFormat());
		try {
			outter.output(doca, new FileWriter(stockCode + ".xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getDate(String desc) {
		String output = "";
		String parts[] = desc.trim().split("\\s+");
		if (parts.length == 3)
			output = parts[1];

		return output;
	}

	private static String getTime(String desc) {
		String output = "";
		String parts[] = desc.trim().split("\\s+");
		if (parts.length == 3)
			output = parts[2];

		return output;
	}
	
/**
 * The default format of XML is with a root element. 
 * However, for this assignment we removed the root using this workaround method
 * @params filename, startline, numlines
 * @return number of lines in the file
 */
	private static int trim(String filename, int startline, int numlines) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			// To store contents of the file
			StringBuffer sb = new StringBuffer("");

			// To keep track of the line number
			int linenumber = 1;
			String line;

			while ((line = br.readLine()) != null) {
				// To store each valid line in the string buffer
				if (linenumber < startline || linenumber >= startline + numlines)
					sb.append(line + "\n");
				linenumber++;
			}

			if (startline + numlines > linenumber)
				System.out.println("End of file reached.");
			br.close();

			FileWriter fw = new FileWriter(new File(filename));
			// To write entire string buffer into the file
			fw.write(sb.toString());
			fw.close();
			return linenumber;
		} catch (Exception e) {
			System.out.println("Something went horribly wrong: " + e.getMessage());
		}
		return 0;
	}

}
