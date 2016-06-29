package Crawler;

import com.example.aturag.shoppingbrowser.MainActivity;
import com.example.aturag.shoppingbrowser.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Product.ExtractDetailFromUrl;
import Product.ProductDetails;
import Product.ProductInfo;

/**
 * Created by Aturag on 20-Jun-16.
 */
public class GetFirstLinkFromGoogle {
    HashMap<String , String> ecommerceTagRegex = new HashMap<>();

    public ArrayList<String> ecommerceUrl,ecommerceName,productTitle;
    public ArrayList<Integer> productEcommerceIcon;
    public String[] ecommerce = {"amazon", "flipkart", "snapdeal", "ebay"};
    private ExtractDetailFromUrl extractDetailFromUrl;
    public GetFirstLinkFromGoogle() {
        ecommerceTagRegex.put("flipkart", "\\/p\\/itm");
        ecommerceTagRegex.put("amazon", "\\/dp\\/");
        ecommerceTagRegex.put("snapdeal", "\\/product\\/");
        ecommerceTagRegex.put("ebay", "\\/itm");
        extractDetailFromUrl = new ExtractDetailFromUrl();
        ecommerceUrl = new ArrayList<>();
        ecommerceName = new ArrayList<>();
        productTitle = new ArrayList<>();
        productEcommerceIcon = new ArrayList<>();
    }

    public void getAllEcommerceUrl(String Url, int queryNumber) {
        ArrayList<ProductInfo> productInfoList = new ArrayList<>();
        MainActivity.datachanged(productInfoList, queryNumber);
        MainActivity._handler.sendEmptyMessage(1);
        for(int i = 0; i < ecommerce.length; i++) {
            try {
                String var = Url + "+" + ecommerce[i];
                crawlGoogle(var, ecommerce[i], i);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for(int i = 0; i < ecommerceUrl.size(); i++ ) {
            System.out.println("Url Url " + ecommerceUrl.get(i) + " " + ecommerceName.get(i) + " " + productTitle.get(i));
            try {
                System.out.println(">>>>> Calling to Product Details " + Url);
                ProductDetails pd = new ProductDetails(ecommerceUrl.get(i), ecommerceName.get(i));
                System.out.println(ecommerceName.get(i) + " abe kuch de toh shi " + pd.getProductPrice() + " " + pd.getProductName());
                if(!pd.getProductName().equals("") && !pd.getProductPrice().equals("")) {
                    ProductInfo productInfo = new ProductInfo();
                    int ma = Math.min(pd.getProductName().length(), 20);
                    productInfo.setName(pd.getProductName());
                    productInfo.Name = productInfo.Name.substring(0, ma);
                    productInfo.setPrice(pd.getProductPrice());
                    productInfo.setEcommerceIcon(productEcommerceIcon.get(i));
                    productInfo.setUrl(ecommerceUrl.get(i));
                    productInfoList.add(productInfo);
                    MainActivity.datachanged(productInfoList, queryNumber);
                    MainActivity._handler.sendEmptyMessage(queryNumber);
                }
                //   String productName = pd.getProductName();
            }
            catch (Exception e) {
                System.out.println("Error in fetching price and name for " + ecommerceName.get(i) + " " + e.getMessage());
                e.printStackTrace();
            }
        }
        /*if(productInfoList.size() > 0) {
            MainActivity.datachanged(productInfoList);
        }*/


    }


    public void crawlGoogle(String Url,String Ecommerce,int index)  {
        ArrayList<String> linksfromGoogle = new ArrayList<>();
        ArrayList<String> textfromGoogle = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(Url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String productUrl = doc.select("div.rc").first().select("a[href]").attr("abs:href");

        if(extractDetailFromUrl.isProductUrl(productUrl)) {
            ecommerceUrl.add(productUrl);
            ecommerceName.add(Ecommerce);
            productEcommerceIcon.add(index);
            productTitle.add(doc.select("div.rc").first().select("a[href]").text());
        } else {
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
    01:10:25.857
            print("\nImports: (%d)", imports.size());
            for (Element link : imports) {
                print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
            }

    */
            System.out.println(doc.select("div.rc").first().select("a[href]"));
            print("\nLinks: (%d)", links.size());
            for (Element link : links) {
                if (link.attr("abs:href").contains(Ecommerce)) {
                    linksfromGoogle.add(link.attr("abs:href"));
                    textfromGoogle.add(link.text().trim());
                }
                //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            }
            System.out.println(linksfromGoogle.size());
            //System.out.println("answer answer !!! " + productPageLink(linksfromGoogle, "amazon"));
            int ans = productPageLink(linksfromGoogle, Ecommerce);
            //System.out.println(Ecommerce + " " + ans);
            if (ans >= 0) {
                ecommerceUrl.add(linksfromGoogle.get(ans));
                ecommerceName.add(Ecommerce);
                productEcommerceIcon.add(index);
                productTitle.add(textfromGoogle.get(ans));
            }
        }
    }


    public int productPageLink(ArrayList<String> links, String Ecommerce) {

        for(int i = 0;i < links.size(); i++) {
            int flag = 0;
            String url = links.get(i);
            Pattern pattern = Pattern.compile(Ecommerce);
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()) {
                if(!ecommerce.equals("")) {
                    String ecommerceRegex = ecommerceTagRegex.get(Ecommerce);
                    pattern = Pattern.compile(ecommerceRegex);
                    matcher = pattern.matcher(url);
                    if(matcher.find()) {
                        return i;
                    }
                    if(ecommerce.equals("amazon")) {
                        ecommerceRegex = "\\/gp\\/";
                        pattern = Pattern.compile(ecommerceRegex);
                        matcher = pattern.matcher(url);
                        if(matcher.find()) {
                            return i;
                        }

                    }
                }
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
