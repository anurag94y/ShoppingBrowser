package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Product.ProductDetails;
import Product.ProductInfo;
import mainactivity.MainActivity;


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

    public ArrayList<ProductInfo> getAllEcommerceUrl(String Url, int queryNumber) throws IOException {
        ArrayList<ProductInfo> ProductInfoList = new ArrayList<>();
        //MainActivity.datachanged(ProductInfoList, queryNumber);
        //MainActivity._handler.sendEmptyMessage(1);
        for(int i = 0; i < ecommerce.length; i++) {
            try {
                String var = Url + "+" + ecommerce[i];
                //resp.getWriter().println(var);
                crawlGoogle(var, ecommerce[i], i);
            } catch (Exception e) {
                //resp.getWriter().println(e.getMessage());
            }
        }

        //resp.getWriter().println(ecommerceUrl.size());

        for(int i = 0; i < ecommerceUrl.size(); i++ ) {
            //resp.getWriter().println("Url Url " + ecommerceUrl.get(i) + " " + ecommerceName.get(i) + " " + productTitle.get(i));
            try {
                //resp.getWriter().println(">>>>> Calling to Product Product Details " + ecommerceUrl.get(i));
                ProductDetails pd = new ProductDetails(ecommerceUrl.get(i), ecommerceName.get(i));
                //resp.getWriter().println(ecommerceName.get(i) + " abe kuch de toh shi " + pd.getProductPrice() + " " + pd.getProductName());
                if(!pd.getProductName().equals("") && !pd.getProductPrice().equals("")) {
                    ProductInfo ProductInfo = new ProductInfo();
                    int ma = Math.min(pd.getProductName().length(), 20);
                    ProductInfo.setName(pd.getProductName());
                    ProductInfo.Name = ProductInfo.Name.substring(0, ma);
                    ProductInfo.setPrice(pd.getProductPrice());
                    ProductInfo.setEcommerceIcon(productEcommerceIcon.get(i));
                    ProductInfo.setUrl(ecommerceUrl.get(i));
                    ProductInfoList.add(ProductInfo);
                    MainActivity.datachanged(ProductInfoList, queryNumber);
                    MainActivity._handler.sendEmptyMessage(queryNumber);
                }
                //   String productName = pd.getProductName();
            }
            catch (Exception e) {
                //resp.getWriter().println("Error in fetching price and name for " + ecommerceName.get(i) + " " + e.getMessage());
                e.printStackTrace();
            }

        }
        /*if(ProductInfoList.size() > 0) {
            MainActivity.datachanged(ProductInfoList, queryNumber);
        }*/

        return ProductInfoList;
    }


    public void crawlGoogle(String Url,String Ecommerce,int index)  {
        try {
            ArrayList<String> linksfromGoogle = new ArrayList<>();
            ArrayList<String> textfromGoogle = new ArrayList<>();
            Document doc = null;
            try {
                Url = Url.replaceAll("[\"\'|]", "");
                doc = Jsoup.connect(Url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36").get();
            } catch (Exception e) {
                //resp.getWriter().println("Error fetching google search with ecommerce domain " + e);
                e.printStackTrace();
            }

            //resp.getWriter().println( "Doc"+  doc + "url " + Url);

            String productUrl = "";
            try {
                productUrl = doc.select("div.rc").first().select("a[href]").attr("abs:href");
            } catch (Exception e) {
                //resp.getWriter().println(e);
            }

            //resp.getWriter().println("Product Url " + productUrl);

            if(extractDetailFromUrl.isProductUrl(productUrl)) {
                ecommerceUrl.add(productUrl);
                ecommerceName.add(Ecommerce);
                productEcommerceIcon.add(index);
                productTitle.add(doc.select("div.rc").first().select("a[href]").text());
            }
            else {
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    if (link.attr("abs:href").contains(Ecommerce)) {
                        linksfromGoogle.add(link.attr("abs:href"));
                        textfromGoogle.add(link.text().trim());
                    }
                    //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
                }
                //resp.getWriter().println(linksfromGoogle.size());
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
        } catch (Exception e) {
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
