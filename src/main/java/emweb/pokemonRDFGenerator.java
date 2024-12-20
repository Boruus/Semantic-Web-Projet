package emweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class pokemonRDFGenerator {
    private static final String NAMESPACE = "http://www.bulbapedia.org/resource/";
    private static final String GEO_NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";

    public static void main(String[] args) {
        try {
            // Appeler la méthode listAllPokemonPages de PokemonPagesFetcher
            List<String> pokemonPages = PokemonPagesFetcher.listPokemonPagesWithKeyword("(Pokémon)");
            int totalPokemon = pokemonPages.size();
            System.out.println("Nombre total de Pokémon récupérés : " + totalPokemon);
    
            // Vérification pour éviter de traiter si la liste est vide
            if (!pokemonPages.isEmpty()) {
                System.out.println("Liste des pages récupérées (premiers 10 résultats) :");
                for (int i = 0; i < Math.min(10, pokemonPages.size()); i++) {
                    System.out.println("- " + pokemonPages.get(i));
                }
    
                // Exemple de traitement sur la première page
                String firstPage = pokemonPages.get(0);
                System.out.println("\nTraitement de la première page : " + firstPage);
                
                String wikiContent = getWikiContent(firstPage);
                // Model model = generateRDFModel(wikiContent, firstPage);
    
                try (FileWriter out = new FileWriter("pokemon_output.ttl")) {
                    // model.write(out, "TURTLE");
                }
                System.out.println("\nRDF data generated and saved to pokemon_output.ttl");
            } else {
                System.out.println("Aucun Pokémon trouvé.");
            }
    
            String fusekiEndpoint = "http://localhost:3030/pokemonDB";
            // loadRDFIntoFuseki(model, fusekiEndpoint);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private static String getWikiContent(String pageTitle) throws Exception {
        String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=parse&page=" +
                     pageTitle + "&format=json&prop=wikitext|links|templates";
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

        return response.toString();
    }

    private static Model generateRDFModel(String wikiContent, String pageTitle) {
        Model model = ModelFactory.createDefaultModel();

        model.setNsPrefix("ex", NAMESPACE);
        model.setNsPrefix("geo", GEO_NS);
        model.setNsPrefix("rdfs", RDFS.getURI());
        model.setNsPrefix("xsd", XSD.getURI());

        String pokemonId = pageTitle.replace(" ", "_");
        String pokemonName = pageTitle;
        String pokemonType = "Grass/Poison"; // Example type
        String latitude = "49.3500"; // Example latitude (not real data)
        String longitude = "8.1406"; // Example longitude (not real data)

        Resource pokemon = model.createResource(NAMESPACE + pokemonId)
                .addProperty(RDF.type, model.createResource(NAMESPACE + "Pokemon"))
                .addProperty(RDFS.label, model.createLiteral(pokemonName, "en"))
                .addProperty(model.createProperty(NAMESPACE, "type"), pokemonType)
                .addLiteral(model.createProperty(GEO_NS, "lat"), model.createTypedLiteral(latitude, XSD.decimal.getURI()))
                .addLiteral(model.createProperty(GEO_NS, "long"), model.createTypedLiteral(longitude, XSD.decimal.getURI()));

        return model;
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
