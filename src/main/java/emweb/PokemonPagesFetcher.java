package emweb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        // Construire l'URL pour la requête avec pagination
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

        // Parse la réponse JSON
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject query = jsonResponse.getJSONObject("query");
        JSONArray allpages = query.getJSONArray("allpages");

        // Filtrer les titres des pages contenant le mot-clé
        for (int i = 0; i < allpages.length(); i++) {
            String title = allpages.getJSONObject(i).getString("title");
            if (title.contains(keyword)) {
                filteredPages.add(title);
            }
        }

        // Mettre à jour `apcontinue` pour la prochaine page
        if (jsonResponse.has("continue")) {
            apContinue = jsonResponse.getJSONObject("continue").getString("apcontinue");
        } else {
            apContinue = null; // Fin de la pagination
        }

    } while (apContinue != null);

    return filteredPages;
}
                                        
                                        

    public static void main(String[] args) {
        try {
            List<String> pokemonPages = PokemonPagesFetcher.listPokemonPagesWithKeyword("(Pokémon)");            
            System.out.println("Nombre total de pages récupérées : " + pokemonPages.size());
            for (String page : pokemonPages) {
                System.out.println(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
