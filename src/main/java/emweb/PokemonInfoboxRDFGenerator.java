package emweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PokemonInfoboxRDFGenerator {

    public static String extractInfobox(String wikiContent, List<String> selectedInfoboxTypes) {
        int startIndex = wikiContent.indexOf("{{");
        while (startIndex != -1) {
            int endIndex = wikiContent.indexOf("}}", startIndex) + 2;
            if (endIndex == -1) {

                System.err.println("Infobox mal formatée");
                return null;
            }
    
            String candidate = wikiContent.substring(startIndex, endIndex);
    
            for (String infoboxType : selectedInfoboxTypes) {
                if (candidate.contains("{{" + infoboxType)) {
                    return candidate; 
                }
            }
    
            startIndex = wikiContent.indexOf("{{", startIndex + 2);
        }

        return null; 
    }

    public static Model generateRDFForTCGPromoInfobox(String infoboxContent, String pageTitle) {
        Model model = ModelFactory.createDefaultModel();
    
        // Définir les préfixes
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("schema", "http://schema.org/");
        model.setNsPrefix("pokemon", "http://pokemon.semanticweb.org/schema#");
    
        // Nettoyer le titre de la page
        String validPageTitle = pageTitle.replaceAll("[^a-zA-Z0-9_]", "_");
    
        // Créer les ressources
        Resource resource = model.createResource("pokemon:" + validPageTitle);
        resource.addProperty(RDF.type, model.createResource("schema:CreativeWork"));
    
        String[] lines = infoboxContent.split("\\|");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String property = keyValue[0].trim();
                String value = keyValue[1].replaceAll("[\\n\\r\\t]+", " ").replaceAll("}}", "").trim();
    
                switch (property.toLowerCase()) {
                    case "setname":
                        resource.addProperty(model.createProperty("schema:name"), value);
                        break;
                    case "cards":
                        resource.addProperty(model.createProperty("schema:numberOfItems"), value);
                        break;
                    case "date":
                        resource.addProperty(model.createProperty("schema:datePublished"), value);
                        break;
                    case "period":
                        resource.addProperty(model.createProperty("schema:temporalCoverage"), value);
                        break;
                    case "logo":
                        resource.addProperty(model.createProperty("schema:logo"), value);
                        break;
                    default:
                        resource.addProperty(model.createProperty("pokemon:" + property), value);
                        break;
                }
            }
        }
    
        return model;
    }
    
    public static Model generateRDFForPokemonInfobox(String infoboxContent, String pageTitle) {
        Model model = ModelFactory.createDefaultModel();
    
        // Définir les préfixes
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("schema", "http://schema.org/");
        model.setNsPrefix("pokemon", "http://pokemon.semanticweb.org/schema#");
    
        // Nettoyer le titre de la page
        String validPageTitle = pageTitle.replaceAll("[^a-zA-Z0-9_]", "_");
    
        // Créer les ressources
        Resource pokemon = model.createResource("pokemon:" + validPageTitle);
        pokemon.addProperty(RDF.type, model.createResource("schema:Thing"));
        pokemon.addProperty(RDFS.label, pageTitle);
    
        String[] lines = infoboxContent.split("\\|");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String property = keyValue[0].trim();
                String value = keyValue[1].replaceAll("[\\n\\r\\t]+", " ").replaceAll("}}", "").trim();
    
                switch (property.toLowerCase()) {
                    case "name":
                        pokemon.addProperty(model.createProperty("schema:name"), value);
                        break;
                    case "type1":
                    case "type2":
                        pokemon.addProperty(model.createProperty("pokemon:type"), value);
                        break;
                    case "ndex":
                        pokemon.addProperty(model.createProperty("pokemon:identifier"), value);
                        break;
                    case "jname":
                        pokemon.addProperty(model.createProperty("pokemon:alternateName"), value);
                        break;
                    case "height-m":
                        pokemon.addProperty(model.createProperty("pokemon:height"), value + "^^xsd:decimal");
                        break;
                    case "weight-kg":
                        pokemon.addProperty(model.createProperty("pokemon:weight"), value + "^^xsd:decimal");
                        break;
                    case "catchrate":
                        pokemon.addProperty(model.createProperty("pokemon:catchRate"), value + "^^xsd:integer");
                        break;
                    default:
                        pokemon.addProperty(model.createProperty("pokemon:" + property), value);
                        break;
                }
            }
        }
    
        return model;
    }
    
    
    public static void addMultilingualLabelsToModel(String tsvFilePath, Model combinedModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(tsvFilePath))) {
            String namespace = "http://www.pokemonDB.org/resource/";
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
    
                if (fields.length >= 4) {
                    String type = fields[0].trim();
                    if ("pokemon".equalsIgnoreCase(type)) {
                        String id = fields[1].trim();
                        String name = fields[2].trim();
                        String language = fields[3].trim(); 
    
                        String langTag = null;
                        switch (language.toLowerCase()) {
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
                                langTag = null;
                                break;
                        }
                        if (langTag != null) {
                            String resourceURI = namespace + id + "_(Pokémon)";
                            Resource pokemonResource = combinedModel.getResource(resourceURI);
    
                            if (pokemonResource != null) {
                                pokemonResource.addProperty(RDFS.label, combinedModel.createLiteral(name, langTag));
                            } else {
                                System.out.println("La ressource " + resourceURI + " n'existe pas dans le modèle. Ignorée.");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Model generateRDFForInfoboxType(String infoboxContent, String pageTitle) {
        if (infoboxContent.contains("TCGPromoInfobox")) {
            return PokemonInfoboxRDFGenerator.generateRDFForTCGPromoInfobox(infoboxContent, pageTitle);
        } else if (infoboxContent.contains("Pokémon Infobox")) {
            return PokemonInfoboxRDFGenerator.generateRDFForPokemonInfobox(infoboxContent, pageTitle);
        } else {
            System.out.println("Aucun générateur RDF trouvé pour l'infobox");
            return null;
        }
    }

    public static void addSameAsLinks(Model model, String pageTitle, List<String> externalLinks) {
        String namespace = "http://www.bulbapedia.org/resource/";
        Resource pokemonResource = model.getResource(namespace + pageTitle + "_(Pokémon)");
    
        for (String link : externalLinks) {
            if (link.contains("dbpedia.org/resource")) {
                pokemonResource.addProperty(model.createProperty("http://www.w3.org/2002/07/owl#sameAs"), model.createResource(link));
            } else if (link.contains("yago-knowledge.org/resource")) {
                pokemonResource.addProperty(model.createProperty("http://www.w3.org/2002/07/owl#sameAs"), model.createResource(link));
            }
        }
    }
    
}