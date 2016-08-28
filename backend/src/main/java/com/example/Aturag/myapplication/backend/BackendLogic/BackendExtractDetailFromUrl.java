package com.example.Aturag.myapplication.backend.BackendLogic;

import java.io.IOException;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Aturag on 20-Jun-16.
 */
public class BackendExtractDetailFromUrl {

    HashMap<String , String> ecommerceTagRegex = new HashMap<>();
    HashMap<String , String> ecommercePresent = new HashMap<>();
    public String ComapnyName = "";

    public BackendExtractDetailFromUrl() {
        ecommerceTagRegex.put("flipkart", "\\/p\\/itm");
        ecommerceTagRegex.put("amazon", "\\/dp\\/");
        ecommerceTagRegex.put("snapdeal", "\\/product\\/");
        ecommerceTagRegex.put("ebay", "\\/itm");
        ecommercePresent.put("flipkart", "1");
        ecommercePresent.put("amazon", "1");
        ecommercePresent.put("snapdeal", "1");
        ecommercePresent.put("ebay", "1");
    }

    public ArrayList<BackendProductInfo> isValidProduct(String Url, int queryNumber, HttpServletResponse resp) throws IOException {
        String ecommerce = findEcommerceName(Url);
        if(isProductUrl(Url, resp)) {
            try {
                //resp.getWriter().println(">>>>> Calling to Product Details " + Url);
                BackendProductDetails pd = new BackendProductDetails(Url, ecommerce, resp);
                String productName = pd.getProductName();
                productName = productName.replaceAll("&"+"nbsp;", " ");
                productName =  productName.replaceAll(String.valueOf((char) 160), " ");
                final String TrimmedUrl = productName.trim().replaceAll(" +", "+");
                final String queryUrl = "https://www.google.com/search?q=" + TrimmedUrl;
                //resp.getWriter().println("!!!!!!!!!! Product name " +  productName + " " + pd.getProductPrice() +" !!!!!!!!!!!!!!!!!!!!!!!!");
                final BackendGetFirstLinkFromGoogle crawler = new BackendGetFirstLinkFromGoogle();
                return crawler.getAllEcommerceUrl(queryUrl, queryNumber,resp);
               /* new AsyncTask<Void, Void, Void>() {
                    String var = "";

                    @Override
                    protected Void doInBackground(Void... params) {
                        crawler.getAllEcommerceUrl(queryUrl);
                        return null;
                    }
                }.execute();*/
                //BackendProductDetails pb = new BackendProductDetails(Url, ecommerce);
            } catch (Exception e) {
                //resp.getWriter().println("Error in BackendExtractDetailFromUrl  " + e.getMessage() + " " + Url  );
                //e.printStackTrace();
            }
        }
        return new ArrayList<BackendProductInfo>();
    }


    public boolean isProductUrl(String Url, HttpServletResponse resp) throws IOException {
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
