package Crawler;

public class Change_Format {
    
    public static String GetString(String S) {
        S.trim();
        int idx = 0;
        int len = S.length();

        while(idx < len && !Character.isDigit(S.charAt(idx))) {
            idx++;
        }

        String nS = "Rs. ";
        while(idx < len) {
            if(S.charAt(idx) == ',') {
                idx++;
                continue;
            }
            nS += S.charAt(idx);
            idx++;
        }
        
        return nS;
    }

}
