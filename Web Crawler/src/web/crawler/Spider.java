/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;
import java.util.*;
/**
 *
 * @author Shiv
 */
public class Spider {
        private static final int MAX_PAGES_TO_SEARCH=20;
    private Set<String> visited_pages=new HashSet<String>();
    private List<String> pages_to_visit=new LinkedList<String>();
   
    private String nextURL()
    {
        String nextURL;
        do
        {
            nextURL=this.pages_to_visit.remove(0);
        }while(this.visited_pages.contains(nextURL));
        this.visited_pages.add(nextURL);
        return nextURL;
    }
    public void search(String url,String word_to_search)
    {
       while(this.visited_pages.size()<MAX_PAGES_TO_SEARCH)
       {
           String currentURL;
           SpiderLeg leg=new SpiderLeg();
           if(this.pages_to_visit.isEmpty())
           {
               currentURL=url;
               this.visited_pages.add(url);
           }
           else
           {
               currentURL=this.nextURL();
           }
           leg.crawl_webpages(currentURL);
           boolean sucess=leg.search_input_word(word_to_search);
           if(sucess)
           {
               System.out.println(String.format("***** Sucess ***** Word %s found at %s",word_to_search,currentURL) );
               break;
           }
           
               this.pages_to_visit.addAll(leg.get_all_links());
       }
       System.out.println(String.format("**Visiting web pages completed** Number of Visited %s web page(s)", this.visited_pages.size()));
    }
    /*
     public static void main(String[] args)
    {
        Spider spider = new Spider();
        spider.search("https://www.codechef.com/", "challenge");
    }
*/
}
