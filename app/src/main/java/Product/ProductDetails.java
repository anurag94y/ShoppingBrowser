package Product;
import android.os.AsyncTask;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.net.*;

import java.io.IOException;


/**
 * Created by paras on 6/20/16.
 */
public class ProductDetails {
    private String product_name = "";
    private String product_price = "";
    private Document doc;

    public String getProductName() {
        return product_name;
    }

    public String getProductPrice() {
        return product_price;
    }

    //For Constructor input will be product's webpage link and company name
    // Output will be nothing :P we have functions for that
    public ProductDetails(String Url_path, String Company_Name) throws IOException{
        switch (Company_Name) {
            case "flipkart":
                Flipkart(Url_path, Company_Name);
                break;
            case "amazon":
                Amazon(Url_path, Company_Name);
                break;
            case "ebay":
                Ebay(Url_path, Company_Name);
                break;
            case "snapdeal":
                Snapdeal(Url_path, Company_Name);
                break;
        }
    }

    boolean IsDesktopSite(String Url_path, String Company_Name) {
        int idx = -1;
        // Is Desktop
        switch (Company_Name) {
            case "flipkart":
                idx = Url_path.indexOf("www.flipkart");
                break;
            case "amazon":
                idx = Url_path.indexOf("www.amazon");
                break;
            case "ebay":
                idx = Url_path.indexOf("www.ebay");
                break;
            case "snapdeal":
                idx = Url_path.indexOf("www.snapdeal");
                break;
        }

        if(idx != -1) return true;

        //Is Mobile site
        switch (Company_Name) {
            case "flipkart":
                idx = Url_path.indexOf("m.flipkart");
                break;
            case "amazon":
                idx = Url_path.indexOf("m.amazon");
                break;
            case "ebay":
                idx = Url_path.indexOf("m.ebay");
                break;
            case "snapdeal":
                idx = Url_path.indexOf("m.snapdeal");
                break;
        }

        if(idx != -1) return false;
        return true;
    }

    public void Flipkart(String Url_path, String Company_Name) throws IOException{
        doc = null;
        Paras_function(Url_path);

        if(doc == null) return;

        boolean is_desktop_site = IsDesktopSite(Url_path, Company_Name); // No need for flipkart

        Element product_deatils = doc.select("div.product-details").first();

        //Extract price div and then price of product
        Element price_div = product_deatils.select("div.prices").first();
        product_price = price_div.select("span.selling-price").first().text();

        //Extract name div of product
        Element name_div = product_deatils.select("div.title-wrap").first();
        product_name = name_div.select("h1.title").first().text();

    }

    public void Amazon(String Url_path, String Company_Name) throws IOException{

        doc = null;
        Paras_function(Url_path);

        if(doc == null) return;

        boolean is_desktop_site = IsDesktopSite(Url_path, Company_Name);  // Also dosen't matter :(

        product_name = doc.select("span#productTitle").first().text();
        product_price = doc.select("div#price").first().select("span.a-color-price").first().text();
    }

    public void Ebay(String Url_path, String Company_Name) throws IOException{

        doc = null;
        Paras_function(Url_path);

        if(doc == null) return;

        boolean is_desktop_site = IsDesktopSite(Url_path, Company_Name);

        if(is_desktop_site) {
            product_name = doc.select("h1#itemTitle").first().text();
            product_price = doc.select("span#prcIsum").first().text();
        } else {
            product_name = doc.select("span.vip-title").first().text();
            product_price = doc.select("span.vip-price").first().text();
        }
    }

    public void Snapdeal(String Url_path, String Company_Name) throws IOException {

        doc = null;
        Paras_function(Url_path);

        if(doc == null) return;

        boolean is_desktop_site = IsDesktopSite(Url_path, Company_Name);

        if(is_desktop_site) {
            Element product_details = doc.select("div.comp-product-description").first();
            product_name = product_details.select("h1").first().text();
            product_price = product_details.select("span.payBlkBig").first().text();
        } else {
            Element product_details = doc.select("div.product-details").first();
            product_name = product_details.select("div.product-title").first().text();
            product_price = product_details.select("span#sdPrice").first().text();
        }
    }

    private void Paras_function(final String Url) {
        try {

            doc = Jsoup.connect(Url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36").get();
           // System.out.println("doc doc " + Url + doc);
        } catch (Exception e) {
            System.out.println("Error in Doc !!!!" +  e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Document document = null;
        document = Jsoup.connect("https://m.snapdeal.com/product/sony-klv32w562d-81-cm-32/626989622357").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36").get();
        Elements element = document.select("#buyPriceBox > div.row.pdp-e-i-PAY > div.pdp-e-i-PAY-r > span:nth-child(2)");
        System.out.println(element.text());
        element = document.select("#productOverview > div.col-xs-13.right-card-zoom > div.comp.comp-product-description > div.pdp-elec-topcenter-inner.layout > div:nth-child(1) > h1");
        System.out.println(element.text());
    }




}
