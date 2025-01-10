package emweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PokemonInfoboxRDFGenerator {
    private static final String NAMESPACE = "http://www.bulbapedia.org/resource/";

    public static String getWikiContent(String pageTitle) throws Exception {
        String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=parse&page=" +
                     pageTitle + "&format=json&prop=wikitext";
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
        return jsonResponse.getJSONObject("parse").getJSONObject("wikitext").getString("*");
    }

    public static String extractInfobox(String wikiContent, List<String> selectedInfoboxTypes) {
        int startIndex = wikiContent.indexOf("{{");
        while (startIndex != -1) {
            int endIndex = wikiContent.indexOf("}}", startIndex) + 2;
            if (endIndex == -1) {

                System.err.println("Infobox mal formatée");
                return null; // Infobox mal formatée
            }
    
            String candidate = wikiContent.substring(startIndex, endIndex);
    
            // Vérifie si le nom de l'infobox correspond à un des types sélectionnés
            for (String infoboxType : selectedInfoboxTypes) {
                if (candidate.contains("{{" + infoboxType)) {
                    System.err.println("Infobox trouvé");

                    return candidate; // Retourne l'infobox correspondante
                }
            }
    
            // Continue la recherche pour d'autres infobox
            startIndex = wikiContent.indexOf("{{", startIndex + 2);
        }
        System.err.println("");

        return null; // Aucune infobox correspondante trouvée
    }

    public static Model generateRDFForTCGPromoInfobox(String infoboxContent, String pageTitle) {
        Model model = ModelFactory.createDefaultModel();
        String namespaceResource = "http://www.bulbapedia.org/resource/";
        String namespacePage = "http://www.bulbapedia.org/page/";
    
        // URI pour la ressource et la page
        Resource resource = model.createResource(namespaceResource + pageTitle.replace(" ", "_"));
        Resource page = model.createResource(namespacePage + pageTitle.replace(" ", "_"));
    
        // Lier la page à la ressource
        page.addProperty(model.createProperty("http://schema.org/about"), resource);
        resource.addProperty(model.createProperty("http://schema.org/isPartOf"), page);
    
        // Extraire les propriétés de l'infobox
        String[] lines = infoboxContent.split("\\|");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String property = keyValue[0].trim();
                String value = keyValue[1].trim();
    
                System.out.println("Property: " + property);
    
                // Mapper les propriétés spécifiques à l'infobox TCGPromoInfobox
                switch (property.toLowerCase()) {
                    case "setname":
                        resource.addProperty(model.createProperty(namespaceResource, "setName"), value);
                        break;
                    case "cards":
                        resource.addProperty(model.createProperty(namespaceResource, "cardsInSet"), value);
                        break;
                    case "date":
                        resource.addProperty(model.createProperty(namespaceResource, "releaseDate"), value);
                        break;
                    case "period":
                        resource.addProperty(model.createProperty(namespaceResource, "releasePeriod"), value);
                        break;
                    case "logo":
                        resource.addProperty(model.createProperty(namespaceResource, "logo"), value);
                        break;
                    case "caption":
                        resource.addProperty(model.createProperty(namespaceResource, "caption"), value);
                        break;
                    default:
                        // Ajouter dynamiquement les autres propriétés
                        resource.addProperty(model.createProperty(namespaceResource, property), value);
                        break;
                }
            }
        }
    
        return model;
    }
    
    
    
    
    public static Model generateRDFModelFromInfobox(String infoboxContent, String pageTitle) {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("ex", NAMESPACE);
    
        String pokemonId = pageTitle.replace(" ", "_");
    
        Resource pokemon = model.createResource(NAMESPACE + pokemonId)
                .addProperty(RDFS.label, pageTitle);
    
        // Extraire les propriétés de l'infobox
        String[] lines = infoboxContent.split("\\|");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String property = keyValue[0].trim();
                String value = keyValue[1].trim();
    
                // Gestion des propriétés courantes
                switch (property.toLowerCase()) {
                    case "name":
                        pokemon.addProperty(model.createProperty(NAMESPACE, "name"), value);
                        break;
                    case "jname":
                        pokemon.addProperty(model.createProperty(NAMESPACE, "japaneseName"), value);
                        break;
                    case "ndex":
                        pokemon.addLiteral(model.createProperty(NAMESPACE, "nationalDex"), value);
                        break;
                    case "type1":
                    case "type2":
                        pokemon.addProperty(model.createProperty(NAMESPACE, "type"), value);
                        break;
                    case "height-m":
                        pokemon.addLiteral(model.createProperty(NAMESPACE, "height"), value);
                        break;
                    case "weight-kg":
                        pokemon.addLiteral(model.createProperty(NAMESPACE, "weight"), value);
                        break;
                    case "ability1":
                    case "ability2":
                    case "abilitym":
                        pokemon.addProperty(model.createProperty(NAMESPACE, "ability"), value);
                        break;
                    case "generation":
                        pokemon.addLiteral(model.createProperty(NAMESPACE, "generation"), value);
                        break;
                    default:
                        // Ajouter dynamiquement les autres propriétés
                        pokemon.addProperty(model.createProperty(NAMESPACE, property), value);
                        break;
                }
            }
        }
    
        return model;
    }
    
}
