package Crawler;

/**
 * Created by Flame Alchemist on 11/8/2015.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Companies {
    private String query;
    Companies(String in ) {
        query = in;
    }

    public void Print(String str) {
        System.out.println("str => " + str);
    }

    public class Flipkart {
        Element ProductInfo;
        Flipkart() {
            try{

                String s1="http://www.flipkart.com/search?q=";
                String s3="&as=on&as-show=on&otracker=start";
                String s="";
                s=s1+query+s3;

                Document doc = null;

                int cnt = 1000000;
                while(cnt-- > 0) {
                    try {
                        doc = Jsoup.connect(s).ignoreHttpErrors(true).timeout(3000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US;   rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
                        break;
                    } catch(UnknownHostException e) {
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException ie) {

                        }
                    } catch(SocketTimeoutException se) {

                    }
                }

                ProductInfo = doc.select("div.product-unit").first();

            } catch (IOException e){
                e.printStackTrace();
            }

        }

        public String GetLink() {
            try {
                Element link = ProductInfo.select("div.pu-visual-section > a").first();
                String Path = link.attr("href");
                String Domain = "http://www.flipkart.com";
                return Domain + Path;
            } catch (NullPointerException np) {
                return "";
            }

        }

        public String GetPrice() {
            try {
                return ProductInfo.select("div.pu-final").first().text();
            } catch(NullPointerException np) {
                return "";
            }

        }

        public String GetName() {
            try {
                return ProductInfo.select("div.pu-title").first().text();
            } catch(NullPointerException npe) {
                return "";
            }
        }
    }

    public class Amazon {
        Element ProductInfo;
        Amazon() {
            try {

                String s = "http://www.amazon.in/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=" + query;
                Document doc = null;
                int cnt = 1000000;
                while(cnt-- >0) {
                    try {
                        doc = Jsoup.connect(s).ignoreHttpErrors(true).timeout(3000).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
                        break;
                    } catch(UnknownHostException e) {
                        //                            logger.log(e.getMessage());
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException ie) {

                        }
                    } catch(SocketTimeoutException se) {

                    }
                }
                ProductInfo = doc.select("#result_0").first();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String GetLink() {
            try {
                Element link = ProductInfo.select("a").first();
                return link.attr("href");
            } catch(NullPointerException np) {
                return "";
            }
        }

        public String GetPrice() {
            try {
                return ProductInfo.select("span.a-color-price").first().text();
            } catch (NullPointerException np) {
                return "";
            }
        }

        public String GetName() {
            try {
                return ProductInfo.select("h2.s-access-title").text();
            } catch (NullPointerException np) {
                return "";
            }
        }
    }

    public class ShopClues {

        Element ProductInfo;
        ShopClues() {
            try{

                String s1 = "http://www.shopclues.com/?subcats=Y&status=A&pname=Y&product_code=Y&match=all&pkeywords=Y&search_performed=Y&sc_z=&z=1&q=";
                String s2 = "&auto_suggest=0&cid=0&dispatch=products.search";
                String s ="";
                s = s1 + query + s2;
                Document doc = null;
                int cnt = 1000000;
                while(cnt-- >0) {
                    try {
                        doc = Jsoup.connect(s).ignoreHttpErrors(true).timeout(3000).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
                        break;
                    } catch(UnknownHostException e) {
                        //                            logger.log(e.getMessage());
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException ie) {

                        }
                    } catch(SocketTimeoutException se) {

                    }
                }
                ProductInfo = doc.select("div.grid-product").first();

            } catch(IOException e){
                e.printStackTrace();
            }

        }

        public String GetLink() {
            try {
                Element E = ProductInfo.select("a").first();
                String Domain = "http://www.shopclues.com";
                String Path = E.attr("href");
                return Domain + Path;
            } catch (NullPointerException np) {
                return "";
            }

        }

        public String GetPrice() {
            try {
                return ProductInfo.select("span.price").first().text();
            } catch (NullPointerException np) {
                return "";
            }
        }

        public String GetName() {
            try {
                return ProductInfo.select("a.name").text();
            } catch (NullPointerException np) {
                return "";
            }
        }

    }

    public class Ebay {
        Element ProductInfo;
        Ebay() {
            try {

                String s1 = "http://www.ebay.in/sch/i.html?_from=R40&_trksid=p2050601.m570.l1313.TR1.TRC0.A0.H0.X";
                String s2 = ".TRS0&_nkw=";
                String s3 = "&_sacat=0";
                String s = s1 + query + s2 + query + s3;

                Document doc = null;
                int cnt = 1000000;
                while(cnt-- > 0) {
                    try {
                        doc = Jsoup.connect(s).ignoreHttpErrors(true).timeout(3000).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
                        break;
                    } catch(UnknownHostException e) {
                        //                            logger.log(e.getMessage());
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException ie) {

                        }
                    } catch (SocketTimeoutException se) {

                    }
                }

                ProductInfo = doc.select("li.sresult").first();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public String GetLink() {
            try {
                Element E = ProductInfo.select("a").first();
                String link = E.attr("href");
                return link;
            } catch (NullPointerException np) {
                return "";
            }

        }

        public String GetPrice() {

            try {
                Element E = ProductInfo.select("li.lvprice").first();
                return E.select("span.bold").first().text();
            } catch (NullPointerException np) {
                return "";
            }
        }

        public String GetName() {
            try {
                return ProductInfo.select("h3.lvtitle").first().text();
            } catch (NullPointerException np) {
                return "";
            }
        }
    }

    public class Snapdeal {
        Element ProductInfo;
        Snapdeal() {
            try{
                String s1 = "http://www.snapdeal.com/search?keyword=";
                String s2 = "&santizedKeyword=&catId=&categoryId=&suggested=false&vertical=&noOfResults=20&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&url=&utmContent=&dealDetail=";
                String s = s1 + query + s2;
                Document doc = null;
                int cnt = 100;
                while(cnt-- > 0) {
                    try {
                        doc = Jsoup.connect(s).ignoreHttpErrors(true).timeout(3000).userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10").get();
                        break;
                    } catch(UnknownHostException e) {
                        //                            logger.log(e.getMessage());
                        try {
                            Thread.sleep(10000);
                        } catch(InterruptedException ie) {

                        }
                    } catch(SocketTimeoutException se) {

                    }
                }
                ProductInfo = doc.select("div.productWrapper").first();


            } catch (IOException e){
                e.printStackTrace();
            }

        }

        public String GetLink() {
            try {
                Element E = ProductInfo.select("a").first();
                String Link = E.attr("href");
                return Link;
            } catch (NullPointerException np) {
                return "";
            }
        }

        public String GetPrice() {
            try {
                return ProductInfo.select("div.product-price").first().select("p").first().text();
            } catch (NullPointerException np) {
                return "";
            }
        }

        public String GetName() {
            try {
                return ProductInfo.select("p.product-title").first().text();
            }  catch (NullPointerException np) {
                return "";
            }
        }
    }

}