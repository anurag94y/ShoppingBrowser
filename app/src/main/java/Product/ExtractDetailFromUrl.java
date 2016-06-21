package Product;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aturag on 20-Jun-16.
 */
public class ExtractDetailFromUrl {

    HashMap<String , String> ecommerceTagRegex = new HashMap<>();
    HashMap<String , String> ecommercePresent = new HashMap<>();

    public ExtractDetailFromUrl() {
        ecommerceTagRegex.put("flipkart", "\\/p\\/itm");
        ecommerceTagRegex.put("amazon", "\\/dp\\/");
        ecommerceTagRegex.put("snapdeal", "\\/product\\/");
        ecommerceTagRegex.put("ebay", "\\/itm\\/");
        ecommercePresent.put("flipkart", "1");
        ecommercePresent.put("amazon", "1");
        ecommercePresent.put("snapdeal", "1");
        ecommercePresent.put("ebay", "1");
    }


    public boolean isProductUrl(String Url) {
        String ecommerce = findEcommerceName(Url);
        if(!ecommerce.equals("")) {
            String ecommerceRegex = ecommerceTagRegex.get(ecommerce);
            Pattern pattern = Pattern.compile(ecommerceRegex);
            Matcher matcher = pattern.matcher(Url);
            if(matcher.find()) {
                return true;
            }
        }
        System.out.println("EcommerceName" + ecommerce);
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
