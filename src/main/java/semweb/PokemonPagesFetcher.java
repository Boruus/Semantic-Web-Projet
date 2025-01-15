package semweb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PokemonPagesFetcher {
    private static final String API_URL = "https://bulbapedia.bulbagarden.net/w/api.php?action=query" +
                                          "&list=allpages&apnamespace=0&aplimit=500&format=json";

    public static List<String> listPokemonPagesWithKeyword(String keyword) throws Exception {
        List<String> filteredPages = new ArrayList<>();
        String apContinue = null;

        do {
            String url = API_URL + (apContinue != null ? "&apcontinue=" + apContinue : "");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "SemanticWebProjectBot/1.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject query = jsonResponse.getJSONObject("query");
            JSONArray allpages = query.getJSONArray("allpages");

            for (int i = 0; i < allpages.length(); i++) {
                String title = allpages.getJSONObject(i).getString("title");
                if (title.contains(keyword)) {
                    filteredPages.add(title);
                }
            }

            if (jsonResponse.has("continue")) {
                apContinue = jsonResponse.getJSONObject("continue").getString("apcontinue");
            } else {
                apContinue = null;
            }

        } while (apContinue != null);

        return filteredPages;
    }


    public static List<String> listAllPages() throws Exception {
        List<String> allPages = new ArrayList<>();
        String apContinue = null;
    
        do {
            String url = API_URL + (apContinue != null ? "&apcontinue=" + apContinue : "");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "SemanticWebProjectBot/1.0");
    
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
    
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
    
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject query = jsonResponse.getJSONObject("query");
            JSONArray allpages = query.getJSONArray("allpages");
    
            for (int i = 0; i < allpages.length(); i++) {
                String title = allpages.getJSONObject(i).getString("title");
                allPages.add(title);
            }
    
            if (jsonResponse.has("continue")) {
                apContinue = jsonResponse.getJSONObject("continue").getString("apcontinue");
            } else {
                apContinue = null;
            }
    
        } while (apContinue != null);
    
        return allPages;
    }


    public static List<String> fetchAllInfoboxTypes() throws Exception {
        List<String> infoboxTypes = new ArrayList<>();
        String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=query" +
                    "&list=categorymembers&cmtitle=Category:Infobox_templates&cmlimit=500&format=json";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "SemanticWebProjectBot/1.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray categoryMembers = jsonResponse.getJSONObject("query").getJSONArray("categorymembers");

        for (int i = 0; i < categoryMembers.length(); i++) {
            String title = categoryMembers.getJSONObject(i).getString("title");
            if (title.startsWith("Template:")) {
                infoboxTypes.add(title.replace("Template:", "").trim());
            }
        }

        return infoboxTypes;
    }

    public static String getWikiContent(String pageTitle) throws Exception {
        String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=parse&page=" +
                    URLEncoder.encode(pageTitle, "UTF-8") + "&format=json&prop=wikitext";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "SemanticWebProjectBot/1.0");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Erreur HTTP " + responseCode + " pour la page : " + pageTitle);
            return null; // Ignore la page si la réponse HTTP n'est pas 200
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseString = response.toString();

        if (!responseString.trim().startsWith("{")) {
            System.out.println("Réponse non valide pour la page : " + pageTitle);
            return null;
        }

        JSONObject jsonResponse = new JSONObject(responseString);
        if (!jsonResponse.has("parse")) {
            System.out.println("Pas de contenu 'parse' trouvé pour la page : " + pageTitle);
            return null;
        }

        String wikitext = jsonResponse.getJSONObject("parse").getJSONObject("wikitext").getString("*");

        if (wikitext.toLowerCase().startsWith("#redirect")) {
            System.out.println("Redirection détectée pour : " + pageTitle);
            String redirectedPage = UtilsFunctions.extractRedirectTarget(wikitext);
            if (redirectedPage != null) {
                System.out.println("Redirigé vers : " + redirectedPage);
                return getWikiContent(redirectedPage);
            }
        }

        return wikitext;
    }


    public static List<String> getExternalLinks(String pageTitle) {
        List<String> externalLinks = new ArrayList<>();
        try {
            String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=parse&page=" +
                    URLEncoder.encode(pageTitle, "UTF-8") + "&format=json&prop=externallinks";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "SemanticWebProjectBot/1.0");
    
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
    
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
    
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("parse") && jsonResponse.getJSONObject("parse").has("externallinks")) {
                JSONArray links = jsonResponse.getJSONObject("parse").getJSONArray("externallinks");
                for (int i = 0; i < links.length(); i++) {
                    externalLinks.add(links.getString(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des liens externes pour " + pageTitle + ": " + e.getMessage());
        }
        return externalLinks;
    }

    
}
