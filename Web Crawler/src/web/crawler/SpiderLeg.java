/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;
 import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author Shiv
 */


public class SpiderLeg {
    
 // We'll use a fake user_web_agent so the web server thinks the robot is a normal web browser and thus allows parsing web pges.
    private static final String user_web_agent =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a search_input_word after the successful crawl_webpages
     * 
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl_webpages was successful
     */
    public boolean crawl_webpages(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(user_web_agent);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode()==200) // 200 is the HTTP OK status code
                                                          // indicating that everything is fine and HTTP status is OK.
            {
                System.out.println("\n*****Visiting***** given input web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure ocuurs ** as retrieved something other than HTML format");
                return false;
            }
            Elements links_on_pages = htmlDocument.select("a[href]");
            System.out.println("Found (" + links_on_pages.size() + ") links on url"+url );
            for(Element link : links_on_pages)
            {
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe)
        {
            //Exception occurs when we don't get succesfull HTTP request
            ioe.printStackTrace();
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl_webpages.
     * 
     * @param word_to_search
     *            - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean search_input_word(String word_to_search)
    {
        // Coding defensively. This method should only be used after a successful crawl_webpages.
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl_webpages() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + word_to_search + "...");
        String text_body = this.htmlDocument.body().text();
        return text_body.toLowerCase().contains(word_to_search.toLowerCase());
    }


    public List<String> get_all_links()
    {
        return this.links;
    }
}
