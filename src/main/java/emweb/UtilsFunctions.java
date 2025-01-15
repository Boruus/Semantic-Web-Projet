package emweb;

public class UtilsFunctions {
    
    public static String extractRedirectTarget(String wikitext) {
        int startIndex = wikitext.indexOf("[[");
        int endIndex = wikitext.indexOf("]]");
    
        if (startIndex != -1 && endIndex != -1) {
            return wikitext.substring(startIndex + 2, endIndex).trim();
        }
    
        return null; 
    }
    
}
