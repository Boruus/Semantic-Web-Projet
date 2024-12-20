package emweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class pokemonRDFGenerator {
    private static final String NAMESPACE = "http://www.bulbapedia.org/resource/";
    private static final String GEO_NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";

    public static void main(String[] args) {
        try {
            String pageTitle = "Bulbasaur"; 
            String wikiContent = getWikiContent(pageTitle);

            Model model = generateRDFModel(wikiContent, pageTitle);

            try (FileWriter out = new FileWriter("pokemon_output.ttl")) {
                model.write(out, "TURTLE");
            }

            System.out.println("RDF data generated and saved to pokemon_output.ttl");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getWikiContent(String pageTitle) throws Exception {
        String url = "https://bulbapedia.bulbagarden.net/w/api.php?action=parse&page=" +
                pageTitle + "&format=json&prop=wikitext";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

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

        // Simulate parsed content (You would extract these from the wikiContent)
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
}
