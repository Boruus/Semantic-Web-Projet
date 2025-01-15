package emweb;

import java.util.List;
import java.util.Map;

public class UtilsFunctions {
    
    public static String extractRedirectTarget(String wikitext) {
        int startIndex = wikitext.indexOf("[[");
        int endIndex = wikitext.indexOf("]]");
    
        if (startIndex != -1 && endIndex != -1) {
            return wikitext.substring(startIndex + 2, endIndex).trim();
        }
    
        return null; 
    }

    public static String findPokemonId(Map<String, List<String[]>> multilingualNamesMap, String pageTitle) {
        if (pageTitle.endsWith(" (Pokémon)")) {
            pageTitle = pageTitle.replace(" (Pokémon)", "").trim();
        }
    
        for (Map.Entry<String, List<String[]>> entry : multilingualNamesMap.entrySet()) {
            for (String[] nameData : entry.getValue()) {
                String name = nameData[1].trim(); 
                if (pageTitle.equalsIgnoreCase(name)) { 
                    return entry.getKey(); 
                }
            }
        }
        return null; 
    }

    public static String getLanguageCode(String language) {
        switch (language.toLowerCase()) {
            case "japanese": return "ja";
            case "korean": return "ko";
            case "chinese": return "zh";
            case "french": return "fr";
            case "german": return "de";
            case "spanish": return "es";
            case "italian": return "it";
            case "english": return "en";
            default: return null; 
        }
    }
    
}
