package Crawler;

import com.example.aturag.shoppingbrowser.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Product.ExtractDetailFromUrl;
import Product.ProductDetails;

/**
 * Created by Aturag on 20-Jun-16.
 */
public class GetFirstLinkFromGoogle {
    public ArrayList<String> ecommerceUrl,ecommerceName,productTitle;
    public String[] ecommerce = {"amazon", "flipkart", "snapdeal", "shopclues", "ebay", "myntra", "voonik", "mrvoonik"};
    private ExtractDetailFromUrl extractDetailFromUrl;
    public GetFirstLinkFromGoogle() {
        extractDetailFromUrl = new ExtractDetailFromUrl();
        ecommerceUrl = new ArrayList<>();
        ecommerceName = new ArrayList<>();
        productTitle = new ArrayList<>();
    }

    public void getAllEcommerceUrl(String Url) {
        for(int i = 0; i < ecommerce.length; i++) {
            String var = Url + "+" + ecommerce[i];
            crawlGoogle(var, ecommerce[i]);
        }

        for(int i = 0; i < ecommerceUrl.size(); i++ ) {
            System.out.println("Url Url " + ecommerceUrl.get(i) + " " + ecommerceName.get(i) + " " + productTitle.get(i));
            try {
                System.out.println(">>>>> Calling to Product Details " + Url);
                ProductDetails pd = new ProductDetails(ecommerceUrl.get(i), ecommerceName.get(i));
                System.out.println(ecommerceName.get(i) + " abe kuch de toh shi " + pd.getProductPrice() + " " + pd.getProductName());
             //   String productName = pd.getProductName();
            }
            catch (Exception e) {
                System.out.println("Error in fetching price and name for " + ecommerceName.get(i) + " " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    public void crawlGoogle(String Url,String Ecommerce)  {
        ArrayList<String> linksfromGoogle = new ArrayList<>();
        ArrayList<String> textfromGoogle = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(Url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

       /* print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }
*/
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            linksfromGoogle.add(link.attr("abs:href"));
            textfromGoogle.add(link.text().trim());
            //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        //System.out.println("answer answer !!! " + productPageLink(linksfromGoogle, "amazon"));
        int ans = productPageLink(linksfromGoogle, Ecommerce);
        //System.out.println(Ecommerce + " " + ans);
        if(ans >= 0) {
            ecommerceUrl.add(linksfromGoogle.get(ans));
            ecommerceName.add(Ecommerce);
            productTitle.add(textfromGoogle.get(ans));
        }
    }


    public int productPageLink(ArrayList<String> links, String ECommerce) {
        String Url = null;
        for(int i = 0;i < links.size(); i++) {
            int flag = 0;
            String pattern = "http://www." +ECommerce;
            String url = links.get(i);
            if(url.length() >= pattern.length()) {
                String ans = url.substring(0, pattern.length());
                //System.out.println(">>>> url " + pattern + " " + ans);
                if(ans.equals(pattern)) {
                    flag = 1;
                }
            }
            pattern = "https://www." +ECommerce;
            if(url.length() >= pattern.length()) {
                String ans = url.substring(0, pattern.length() );
                //System.out.println(">>>> url " + pattern + " " + ans);
                if(ans.equals(pattern)) {
                    flag = 1;
                }
            }

            pattern = "http://m." +ECommerce;
            if(url.length() >= pattern.length()) {
                String ans = url.substring(0, pattern.length() );
                //System.out.println(">>>> url " + pattern + " " + ans);
                if(ans.equals(pattern)) {
                    flag = 1;
                }
            }

            pattern = "https://m." +ECommerce;
            if(url.length() >= pattern.length()) {
                String ans = url.substring(0, pattern.length() );
                //System.out.println(">>>> url " + pattern + " " + ans);
                if(ans.equals(pattern)) {
                    flag =1;
                }
            }
            if(flag == 1 && extractDetailFromUrl.isProductUrl(url)) {
                return i;
            }

        }
        return -1;
    }



    private void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }


}
