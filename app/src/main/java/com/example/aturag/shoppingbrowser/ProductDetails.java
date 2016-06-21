package com.example.aturag.shoppingbrowser;
import android.os.AsyncTask;

import org.jsoup.*;
import org.jsoup.nodes.*;

import java.net.*;

import java.io.IOException;


/**
 * Created by paras on 6/20/16.
 */
public class ProductDetails {
    private String product_name;
    private String product_price;
    private Document doc;
    private boolean lock_variable;

    public String getProductName() {
        return product_name;
    }

    public String getProductPrice() {
        return product_price;
    }

    //For Constructor input will be product's webpage link and company name
    // Output will be nothing :P we have functions for that
    ProductDetails(String Url_path, String Company_Name) throws IOException{
        switch (Company_Name) {
            case "Flipkart":
                Flipkart(Url_path);
                break;
            case "Amazon":
                Amazon(Url_path);
                break;
            case "Ebay":
                Ebay(Url_path);
                break;
            case "Snapdeal":
                Snapdeal(Url_path);
                break;
        }
    }

    public void Flipkart(String Url_path) throws IOException{

        System.out.println("-------------------------Flipkart me aaya----------------------");

        doc = null;
        lock_variable = false;
        Paras_function(Url_path);
        while(lock_variable == false) {
            // Do something :P
        }

        System.out.println("-------------------------lock variable true ho gya ---------------");


        Element product_deatils = doc.select("div.product-details").first();

        //Extract price div and then price of product
        Element price_div = product_deatils.select("div.prices").first();
        product_price = price_div.select("span.selling-price").first().text();

        //Extract name div of product
        Element name_div = product_deatils.select("div.title-wrap").first();
        product_price = name_div.select("h1.title").first().text();

    }

    public void Amazon(String Url_path) throws IOException{

        System.out.println("-------------------------Amazon me aaya----------------------");

        doc = null;
        lock_variable = false;
        Paras_function(Url_path);
        while(lock_variable == false) {
            // Do something :P
        }

        System.out.println("-------------------------lock variable true ho gya ---------------");


        product_name = doc.select("span.productTitle").first().text();
        product_price = doc.select("span.priceblock_ourprice").first().text();
    }

    public void Ebay(String Url_path) throws IOException{

        System.out.println("-------------------------Ebay me aaya----------------------");

        doc = null;
        lock_variable = false;
        Paras_function(Url_path);
        while(lock_variable == false) {
            // Do something :P
        }

        System.out.println("-------------------------lock variable true ho gya ---------------");


        product_name = doc.select("h1.itemTitle").first().text();
        product_price = doc.select("span.prcIsum").first().text();
    }

    public void Snapdeal(String Url_path) throws IOException{

        System.out.println("-------------------------Snapdeal me aaya----------------------");

        doc = null;
        lock_variable = false;
        Paras_function(Url_path);
        while(lock_variable == false) {
            // Do something :P
        }

        System.out.println("-------------------------lock variable true ho gya ---------------");

        Element product_details = doc.select("div.comp-product-description").first();

        product_name = product_details.select("h1").first().text();
        product_price = product_details.select("span.payBlkBig").first().text();
    }

    private void Paras_function(final String url) {
        new AsyncTask<Void,Void ,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int cnt = 30;
                while(cnt-- > 0) {
                    try {
                        doc = Jsoup.connect(url).get();
                        System.out.println("doc doc " + url + " " + doc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("doc error " + url + " " + e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                System.out.print("-----------------------Bhodi kkkkkkk -----------------");
                lock_variable = true;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute();
    }

}
