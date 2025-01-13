package emweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class pokemonRDFGenerator {
    public static void main(String[] args) {
        try {
            //List<String> pokemonPages = PokemonPagesFetcher.listPokemonPagesWithKeyword("(Pokémon)");
           
            // On query les pages de toute la DB
            List<String> pokemonPages = PokemonPagesFetcher.listAllPages();
            System.out.println("Nombre total de Pokémon récupérés : " + pokemonPages.size());

            // On query tous les types d'infoboxes
            List<String> AllInfoboxesTypes = fetchAllInfoboxTypes();

            // On sélectionne les types d'infoboxes
            List<String> selectedInfoboxTypes = new ArrayList<>(AllInfoboxesTypes);
            selectedInfoboxTypes.add("PokémonInfobox"); // Ajouter le type spécifique
            selectedInfoboxTypes.add("PokémonInfoboxNoncat"); // Ajouter le type spécifique
            selectedInfoboxTypes.add("Pokémon_Infobox"); // Ajouter le type spécifique

            System.out.println("Nombre de Pokémon avec infoboxes sélectionnées : " + pokemonPages.size());
            // On crée un modèle RDF pour chaque page de Pokémon
            Model combinedModel = ModelFactory.createDefaultModel();

            int counter = 0;
            for (String pageTitle : pokemonPages) {
                if (counter >= 100) {
                    break; 
                }
                System.out.println("Traitement de la page : " + pageTitle);

                String wikiContent = getWikiContent(pageTitle);
                //System.out.println("Contenu de la page : " + wikiContent);
                if (wikiContent != null && !wikiContent.isEmpty()) {
                    System.out.println("Extraction de l'infobox pour : " + selectedInfoboxTypes);
                    String infoboxContent = PokemonInfoboxRDFGenerator.extractInfobox(wikiContent, selectedInfoboxTypes);
                    if (infoboxContent != null) {
                        Model model = PokemonInfoboxRDFGenerator.generateRDFForPokemonInfobox(infoboxContent, pageTitle);
                        combinedModel.add(model); 
                    } else {
                        System.out.println("1 " + pageTitle);
                    }
                } else {
                    System.out.println("2 " + pageTitle);
                }
                counter++;
            }

            try (FileWriter out = new FileWriter("pokemon_output.ttl")) {
                combinedModel.write(out, "TURTLE");
                System.out.println("Données RDF sauvegardées dans le fichier pokemon_output.ttl");
            }

            String fusekiEndpoint = "http://localhost:3030/pokeDB";
            loadRDFIntoFuseki(combinedModel, fusekiEndpoint);

            System.out.println("\nToutes les données RDF ont été chargées dans Fuseki.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractRedirectTarget(String wikitext) {
        int startIndex = wikitext.indexOf("[[");
        int endIndex = wikitext.indexOf("]]");
    
        if (startIndex != -1 && endIndex != -1) {
            return wikitext.substring(startIndex + 2, endIndex).trim();
        }
    
        return null; 
    }

    public static List<String> filterPagesWithInfoboxTypes(List<String> allPages, List<String> selectedInfoboxTypes) throws Exception {
        List<String> filteredPages = new ArrayList<>();
        for (String pageTitle : allPages) {
            String wikiContent = PokemonInfoboxRDFGenerator.getWikiContent(pageTitle);
            if (wikiContent != null && !wikiContent.isEmpty()) {
                for (String infoboxType : selectedInfoboxTypes) {
                    if (wikiContent.contains("{{" + infoboxType)) {
                        filteredPages.add(pageTitle);
                        break; // Passe à la page suivante si une infobox correspond
                    }
                }
            }
        }
        return filteredPages;
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

        // Vérifie si la réponse est un JSON valide
        if (!responseString.trim().startsWith("{")) {
            System.out.println("Réponse non valide pour la page : " + pageTitle);
            return null; // Ignore la page si ce n'est pas un JSON valide
        }

        JSONObject jsonResponse = new JSONObject(responseString);
        if (!jsonResponse.has("parse")) {
            System.out.println("Pas de contenu 'parse' trouvé pour la page : " + pageTitle);
            return null; // Ignore si la clé 'parse' est absente
        }

        String wikitext = jsonResponse.getJSONObject("parse").getJSONObject("wikitext").getString("*");

        if (wikitext.toLowerCase().startsWith("#redirect")) {
            System.out.println("Redirection détectée pour : " + pageTitle);
            String redirectedPage = extractRedirectTarget(wikitext);
            if (redirectedPage != null) {
                System.out.println("Redirigé vers : " + redirectedPage);
                return getWikiContent(redirectedPage); // Appel récursif pour la redirection
            }
        }

        return wikitext;
    }


    private static void loadRDFIntoFuseki(Model model, String fusekiEndpoint) {
        try (RDFConnection conn = RDFConnectionFactory.connect(fusekiEndpoint)) {
            conn.load(model); 
            System.out.println("RDF data loaded into Fuseki at " + fusekiEndpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}