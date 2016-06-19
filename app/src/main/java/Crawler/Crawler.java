package Crawler;

import android.util.Log;


import java.util.Vector;

/**
 * Created by Flame Alchemist on 11/10/2015.
 */
public class Crawler {
    private Vector<String> Names,Links,Prices;
    private Vector<String> flipkart, amazon, shopclues, snapdeal, ebay;
    String query;
    public Crawler(String Query){

        query=Query;
        Names = new Vector<>();
        Links = new Vector<>();
        Prices = new Vector<>();
        /*Names.add(query);
        Prices.add(query);
        Links.add(query);*/
    }

    public void Crawl() throws InterruptedException {
        final Change_Format change_format = new Change_Format();
        Names = new Vector<>();
        Links = new Vector<>();
        Prices = new Vector<>();
        flipkart = new Vector<>();
        ebay = new Vector<>();
        amazon = new Vector<>();
        shopclues = new Vector<>();
        snapdeal = new Vector<>();
        final Companies companies = new Companies(query);
        Log.e("Crawler","In Crawler");
        Thread thread1 = new Thread("Thread1") {
            public void run() {
            //    flipkart = new Vector<String>();
                Companies.Flipkart fp = companies.new Flipkart();
                flipkart.add(fp.GetName());
                flipkart.add(change_format.GetString(fp.GetPrice()));
                flipkart.add(fp.GetLink());
                System.out.println(flipkart.get(0) + flipkart.get(1) + flipkart.get(2) + "------------ Flipkart -------- \n");
            }
        };

        Thread thread2 = new Thread("Thread2") {
            public void run() {

            //    amazon = new Vector<String>();
                Companies.Amazon amz = companies.new Amazon();
                amazon.add(amz.GetName());
                amazon.add(change_format.GetString(amz.GetPrice()));
                amazon.add(amz.GetLink());
                System.out.println(amazon.get(0) + amazon.get(1) +amazon.get(2)+"------------ Amazon -------- \n");
            }
        };

        Thread thread5 = new Thread("Thread5") {
            public void run() {
             //   shopclues = new Vector<String>();
                Companies.ShopClues sc = companies.new ShopClues();
                shopclues.add(sc.GetName());
                shopclues.add(change_format.GetString(sc.GetPrice()));
                shopclues.add(sc.GetLink());
                System.out.println(shopclues.get(0) + shopclues.get(1) + shopclues.get(2) + "------------ shopclues -------- \n");
            }
        };

        Thread thread3 = new Thread("Thread3") {
            public void run() {

            //    ebay = new Vector<String>();
                Companies.Ebay eb = companies.new Ebay();
                ebay.add(eb.GetName());
                ebay.add(change_format.GetString(eb.GetPrice()));
                ebay.add(eb.GetLink());
                System.out.println(ebay.get(0) + ebay.get(1) + ebay.get(2) + "------------ ebay -------- \n");
            }
        };

        Thread thread4 = new Thread("Thread4") {
            public void run() {

            //    snapdeal = new Vector<String>();
                Companies.Snapdeal sd = companies.new Snapdeal();
                snapdeal.add(sd.GetName());
                snapdeal.add(change_format.GetString(sd.GetPrice()));
                snapdeal.add(sd.GetLink());
                System.out.println(snapdeal.get(0) + snapdeal.get(1) + snapdeal.get(2) + "------------ snapdeal -------- \n");
            }
        };

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e){

        }

        AddResult();
        return;
    }

    public void AddResult(){
        if( !flipkart.get(0).isEmpty() && !flipkart.get(1).isEmpty() && !flipkart.get(2).isEmpty() ){
            Names.add(flipkart.get(0));
            Prices.add(flipkart.get(1));
            Links.add(flipkart.get(2));
        }

        if( !amazon.isEmpty() && !amazon.get(0).isEmpty() && !amazon.get(1).isEmpty() && !amazon.get(2).isEmpty() ){
            Names.add(amazon.get(0));
            Prices.add(amazon.get(1));
            Links.add(amazon.get(2));
        }

        if( !snapdeal.isEmpty() && !snapdeal.get(0).isEmpty() && !snapdeal.get(1).isEmpty() && !snapdeal.get(2).isEmpty() ){
            Names.add(snapdeal.get(0));
            Prices.add(snapdeal.get(1));
            Links.add(snapdeal.get(2));
        }

        if( !ebay.isEmpty() && !ebay.get(0).isEmpty() && !ebay.get(1).isEmpty() && !ebay.get(2).isEmpty() ){
            Names.add(ebay.get(0));
            Prices.add(ebay.get(1));
            Links.add(ebay.get(2));
        }

        if( !shopclues.isEmpty() && !shopclues.get(0).isEmpty() && !shopclues.get(1).isEmpty() && !shopclues.get(2).isEmpty() ){
            Names.add(shopclues.get(0));
            Prices.add(shopclues.get(1));
            Links.add(shopclues.get(2));
        }
    }

    public Vector<String> GetNames(){
        return Names;
    }

    public Vector<String> GetLinks(){
        return Links;
    }

    public Vector<String> GetPrices(){
        return Prices;
    }

}
