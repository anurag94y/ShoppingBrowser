package Product;

import java.util.HashMap;

/**
 * Created by Aturag on 20-Jun-16.
 */
public class ExtractDetailFromUrl {

    HashMap<String , String> ecommerceTagRegex = new HashMap<>();

    public ExtractDetailFromUrl() {
        ecommerceTagRegex.put("flipkart", "\\/p\\/itm");
        ecommerceTagRegex.put("amazon", "\\/dp\\/");
        ecommerceTagRegex.put("snapdeal", "\\/product\\/");
        ecommerceTagRegex.put("ebay", "\\/itm\\/");

    }


    public boolean isProductUrl(String Url) {
        String ecommerceName = findEcommerceName(Url);

        return false;
    }

    private String findEcommerceName(String url) {
        return null;
    }
}
