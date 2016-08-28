package Crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Product.ProductDetails;
import Product.ProductInfo;


/**
 * Created by Aturag on 20-Jun-16.
 */
public class ExtractDetailFromUrl {

    HashMap<String , String> ecommerceTagRegex = new HashMap<>();
    HashMap<String , String> ecommercePresent = new HashMap<>();
    public String ComapnyName = "";

    public ExtractDetailFromUrl() {
        ecommerceTagRegex.put("flipkart", "\\/p\\/itm");
        ecommerceTagRegex.put("amazon", "\\/dp\\/");
        ecommerceTagRegex.put("snapdeal", "\\/product\\/");
        ecommerceTagRegex.put("ebay", "\\/itm");
        ecommercePresent.put("flipkart", "1");
        ecommercePresent.put("amazon", "1");
        ecommercePresent.put("snapdeal", "1");
        ecommercePresent.put("ebay", "1");
    }

    public ArrayList<ProductInfo> isValidProduct(String Url, int queryNumber) throws IOException {
        String ecommerce = findEcommerceName(Url);
        if(isProductUrl(Url)) {
            try {
                //resp.getWriter().println(">>>>> Calling to Product Details " + Url);
                ProductDetails pd = new ProductDetails(Url, ecommerce);
                String productName = pd.getProductName();
                productName = productName.replaceAll("&"+"nbsp;", " ");
                productName =  productName.replaceAll(String.valueOf((char) 160), " ");
                final String TrimmedUrl = productName.trim().replaceAll(" +", "+");
                final String queryUrl = "https://www.google.com/search?q=" + TrimmedUrl;
                //resp.getWriter().println("!!!!!!!!!! Product name " +  productName + " " + pd.getProductPrice() +" !!!!!!!!!!!!!!!!!!!!!!!!");
                final GetFirstLinkFromGoogle crawler = new GetFirstLinkFromGoogle();
                //return crawler.getAllEcommerceUrl(queryUrl, queryNumber);
               /* new AsyncTask<Void, Void, Void>() {
                    String var = "";

                    @Override
                    protected Void doInBackground(Void... params) {
                        crawler.getAllEcommerceUrl(queryUrl);
                        return null;
                    }
                }.execute();*/
                //ProductDetails pb = new ProductDetails(Url, ecommerce);
            } catch (Exception e) {
                //resp.getWriter().println("Error in ExtractDetailFromUrl  " + e.getMessage() + " " + Url  );
                //e.printStackTrace();
            }
        }
        return new ArrayList<ProductInfo>();
    }


    public boolean isProductUrl(String Url) throws IOException {
        String ecommerce = findEcommerceName(Url);
        //resp.getWriter().println("EcommerceName ->" + ecommerce);
        if(!ecommerce.equals("")) {
            String ecommerceRegex = ecommerceTagRegex.get(ecommerce);
            Pattern pattern = Pattern.compile(ecommerceRegex);
            Matcher matcher = pattern.matcher(Url);
            if(matcher.find()) {
                return true;
            }
            if(ecommerce.equals("amazon")) {
                ecommerceRegex = "\\/gp\\/";
                pattern = Pattern.compile(ecommerceRegex);
                matcher = pattern.matcher(Url);
                if(matcher.find()) {
                    return true;
                }

            }
        }
        //System.out.println("EcommerceName" + ecommerce);
        return false;
    }

    private String findEcommerceName(String Url) {
        String ans = "";
        for(int i = 10;i < Url.length() && Url.charAt(i) != '.'; i++) {
            ans = ans + Url.charAt(i);
        }
        if(ecommercePresent.containsKey(ans))
            return ans;
        ans = "";
        for(int i = 11;i < Url.length() && Url.charAt(i) != '.'; i++) {
            ans = ans + Url.charAt(i);
        }
        if(ecommercePresent.containsKey(ans))
            return ans;

        ans = "";
        for(int i = 8;i < Url.length() && Url.charAt(i) != '.'; i++) {
            ans = ans + Url.charAt(i);
        }
        if(ecommercePresent.containsKey(ans))
            return ans;

        ans = "";
        for(int i = 9;i < Url.length() && Url.charAt(i) != '.'; i++) {
            ans = ans + Url.charAt(i);
        }
        if(ecommercePresent.containsKey(ans))
            return ans;
        return "";
    }
}

