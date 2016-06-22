package Product;
import android.os.AsyncTask;

import org.jsoup.*;
import org.jsoup.nodes.*;

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
                Flipkart(Url_path);
                break;
            case "amazon":
                Amazon(Url_path);
                break;
            case "ebay":
                Ebay(Url_path);
                break;
            case "snapdeal":
                Snapdeal(Url_path);
                break;
        }
    }

    public void Flipkart(String Url_path) throws IOException{

        System.out.println("-------------------------Flipkart me aaya----------------------");

        doc = null;
        Paras_function(Url_path);
        System.out.println("-------------------------lock variable true ho gya ---------------");


        Element product_deatils = doc.select("div.product-details").first();

        System.out.println("---------------------product_details----------------\n" + product_deatils);



        //Extract price div and then price of product
        Element price_div = product_deatils.select("div.prices").first();
        product_price = price_div.select("span.selling-price").first().text();

        //Extract name div of product
        Element name_div = product_deatils.select("div.title-wrap").first();
        product_name = name_div.select("h1.title").first().text();

    }

    public void Amazon(String Url_path) throws IOException{

        System.out.println("-------------------------Amazon me aaya----------------------");

        doc = null;
        Paras_function(Url_path);

        System.out.println("-------------------------lock variable true ho gya ---------------");


        product_name = doc.select("span#productTitle").first().text();
        product_price = doc.select("div#price").first().select("span.a-color-price").first().text();;
    }

    public void Ebay(String Url_path) throws IOException{

        System.out.println("-------------------------Ebay me aaya----------------------");

        doc = null;
        Paras_function(Url_path);
        System.out.println("-------------------------lock variable true ho gya ---------------");


        product_name = doc.select("h1#itemTitle").first().text();
        product_price = doc.select("span#prcIsum").first().text();
    }

    public void Snapdeal(String Url_path) throws IOException {

        System.out.println("-------------------------Snapdeal me aaya----------------------");

        doc = null;
        Paras_function(Url_path);
        System.out.println("-------------------------lock variable true ho gya ---------------");
        Element product_details = doc.select("div.comp-product-description").first();

        System.out.println("---------------------product_details----------------\n"+ product_details);

        product_name = product_details.select("h1").first().text();
        product_price = product_details.select("span.payBlkBig").first().text();
    }

    private void Paras_function(final String Url) {
        try {

            doc = Jsoup.connect(Url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36").get();
            System.out.println("doc doc " + Url + doc);
        } catch (Exception e) {
            System.out.println("Error in Snapdeal Doc !!!!" +  e.getMessage());
        }
    }




}
