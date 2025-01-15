package semweb;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.util.Arrays;
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
    
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("schema", "http://schema.org/");
        model.setNsPrefix("pokemon", "http://pokemon.semanticweb.org/schema#");
    
        String validPageTitle = pageTitle.replaceAll("[^a-zA-Z0-9_]", "_");
    
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

    public static Model generateRDFForPokemonInfobox(String infoboxContent, String pageTitle, List<String[]> multilingualNames, String pokemonId) {
        Model model = ModelFactory.createDefaultModel();
    
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("schema", "http://schema.org/");
        model.setNsPrefix("pokemon", "http://pokemon.semanticweb.org/ressource#");
        model.setNsPrefix("wiki", "http://pokemon.semanticweb.org/page#");
    
        String validPageTitle = pageTitle.replaceAll("[^a-zA-Z0-9_]", "_");
    
        Resource pokemon = model.createResource("pokemon:" + validPageTitle);
        pokemon.addProperty(RDF.type, model.createResource("schema:Thing"));
    
        Resource wikiPage = model.createResource("wiki:" + validPageTitle);
        pokemon.addProperty(model.createProperty("schema:about"), wikiPage);
    
        try {
            List<String> externalLinks = PokemonPagesFetcher.getExternalLinks(pageTitle);
    
            for (String link : externalLinks) {
                if (link.startsWith("http://dbpedia.org/resource/")) {
                    pokemon.addProperty(model.createProperty("owl:sameAs"), model.createResource(link));
                } else if (link.startsWith("http://yago-knowledge.org/resource/")) {
                    pokemon.addProperty(model.createProperty("owl:sameAs"), model.createResource(link));
                } else {
                    pokemon.addProperty(model.createProperty("schema:url"), model.createResource(link));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des liens externes pour " + pageTitle + ": " + e.getMessage());
        }
    
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
                    default:
                        pokemon.addProperty(model.createProperty("pokemon:" + property), value);
                        break;
                }
            }
        }
    
        addMultilingualNames(pokemon, multilingualNames, pokemonId);
    
        return model;
    }
    
    public static void addMultilingualNames(Resource pokemon, List<String[]> names, String pokemonId) {
        for (String[] nameData : names) {
            if (nameData.length >= 3) {
                String id = nameData[0].trim();
                String name = nameData[1].trim();
                String language = nameData[2].trim();
                String langCode = UtilsFunctions.getLanguageCode(language);
                
                System.out.println(pokemonId + " " + id + " " + name + " " + language + " " + langCode);
                if (id.equals(pokemonId) && langCode != null) {
                    pokemon.addProperty(
                        pokemon.getModel().createProperty("schema:name"),
                        pokemon.getModel().createLiteral(name, langCode)
                    );
                }
            } else {
                System.out.println("Format incorrect pour nameData : " + Arrays.toString(nameData));
            }
        }
    }
    
    public static Model generateRDFForInfoboxType(String infoboxContent, String pageTitle, List<String[]> multilingualNames, String pokemonId) {
        if (infoboxContent.contains("TCGPromoInfobox")) {
            return PokemonInfoboxRDFGenerator.generateRDFForTCGPromoInfobox(infoboxContent, pageTitle);
        } else if (infoboxContent.contains("Pokémon Infobox")) {
            return PokemonInfoboxRDFGenerator.generateRDFForPokemonInfobox(infoboxContent, pageTitle, multilingualNames, pokemonId);
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
