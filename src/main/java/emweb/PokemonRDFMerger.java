package emweb;
import java.io.*;
import java.util.*;

public class PokemonRDFMerger {
    public static void main(String[] args) {
        // Fichiers d'entrée et de sortie
        String existingTripletsFile = "pokemon_output.ttl";
        String multilingualFile = "output/pokemon_graph.ttl";
        String outputFile = "output/updated_pokemon.ttl";

        try {
            // Charger les triplets existants
            Map<String, String> existingURIs = loadExistingURIs(existingTripletsFile);
            System.out.println("existingURIs " + existingURIs);


            // Charger les noms multilingues
            Map<String, List<String>> multilingualLabels = loadMultilingualLabels(multilingualFile);

            // Fusionner et générer les nouveaux triplets
            generateUpdatedRDF(existingURIs, multilingualLabels, outputFile);

            System.out.println("Fichier RDF mis à jour : " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Charger les URI des Pokémon existants
   // Charger les URI des Pokémon existants
private static Map<String, String> loadExistingURIs(String filePath) throws IOException {
    Map<String, String> uris = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        String currentURI = null;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("<http://")) {
                currentURI = line.trim();
            } else if (line.contains("<http://schema.org/nationalDexNumber>")) {
                String[] parts = line.split("\"");
                if (parts.length > 1) {
                    String ndex = parts[1]; // Extraire le Ndex
                    uris.put(ndex, currentURI);
                }
            }
        }
    }
    return uris;
}
    // Charger les labels multilingues
    private static Map<String, List<String>> loadMultilingualLabels(String filePath) throws IOException {
        Map<String, List<String>> labels = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 4) continue;

                String ndex = parts[1];
                String name = parts[2];
                String lang = parts[3].toLowerCase();

                String langTag;
                switch (lang) {
                    case "french":
                        langTag = "fr";
                        break;
                    case "german":
                        langTag = "de";
                        break;
                    case "spanish":
                        langTag = "es";
                        break;
                    case "italian":
                        langTag = "it";
                        break;
                    case "english":
                        langTag = "en";
                        break;
                    case "japanese":
                        langTag = "ja";
                        break;
                    case "korean":
                        langTag = "ko";
                        break;
                    case "chinese":
                        langTag = "zh";
                        break;
                    default:
                        langTag = "unknown";
                        break;
                }

                String rdfLabel = "\"" + name + "\"@" + langTag;
                labels.computeIfAbsent(ndex, k -> new ArrayList<>()).add(rdfLabel);
            }
        }
        return labels;
    }

    // Générer le fichier RDF mis à jour
    private static void generateUpdatedRDF(Map<String, String> existingURIs, Map<String, List<String>> multilingualLabels, String outputFile) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, String> entry : existingURIs.entrySet()) {
                String ndex = entry.getKey();
                String uri = entry.getValue();

                bw.write(uri + "\n");

                // Ajouter les labels existants
                if (multilingualLabels.containsKey(ndex)) {
                    for (String label : multilingualLabels.get(ndex)) {
                        bw.write("    <http://www.w3.org/2000/01/rdf-schema#label> " + label + " ;\n");
                    }
                }

                // Fermer l'entité
                bw.write(".\n\n");
            }
        }
    }
}
