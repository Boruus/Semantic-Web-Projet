package emweb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TsvReader {
    public static Map<String, List<String[]>> readMultilingualNamesFromTSV(String filePath) throws IOException {
        Map<String, List<String[]>> multilingualNames = new HashMap<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    String type = parts[0].trim();
                    String id = parts[1].trim();
                    String name = parts[2].trim();
                    String language = parts[3].trim();
    
                    if ("pokemon".equalsIgnoreCase(type)) {
                        multilingualNames.putIfAbsent(id, new ArrayList<>());
                        multilingualNames.get(id).add(new String[]{id, name, language});
                    }
                } else {
                    System.out.println("Ligne mal formatée ignorée : " + line);
                }
            }
        }
        return multilingualNames;
    }
    
    
    
}
