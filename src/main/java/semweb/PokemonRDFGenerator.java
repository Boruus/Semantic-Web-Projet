package semweb;

import org.apache.jena.rdf.model.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PokemonRDFGenerator {
    public static void main(String[] args) {
        try {              
            List<String> allPages = PokemonPagesFetcher.listAllPages();
            List<String> first1000Pages = allPages.subList(0, Math.min(100, allPages.size()));
            List<String> validPokemonNames = PokemonNames.getValidPokemonNames();
            List<String> pokemonWithSuffix = validPokemonNames.stream().map(name -> name + " (Pokémon)").collect(Collectors.toList());
            List<String> pokemonPages = PokemonPagesFetcher.listPokemonPagesWithKeyword("(Pokémon)");
            List<String> selectPokemonPages = pokemonPages.stream().filter(pokemonWithSuffix::contains).collect(Collectors.toList());
            List<String> AllInfoboxesTypes = PokemonPagesFetcher.fetchAllInfoboxTypes();
            List<String> selectedInfoboxTypes = new ArrayList<>(AllInfoboxesTypes.subList(0, Math.min(2, AllInfoboxesTypes.size())));
            selectedInfoboxTypes.add("TCGPromoInfobox"); 
            selectedInfoboxTypes.add("Pokémon Infobox");

            Set<String> combinedPages = new HashSet<>(selectPokemonPages);
            combinedPages.addAll(first1000Pages);

            Model combinedModel = ModelFactory.createDefaultModel();

            String tsvFilePath = "src/main/resources/external_data/pokedex-i18n.tsv";
            Map<String, List<String[]>> multilingualNamesMap = TsvReader.readMultilingualNamesFromTSV(tsvFilePath);
                    
            for (String pageTitle : combinedPages) {            
                String wikiContent = PokemonPagesFetcher.getWikiContent(pageTitle);
                
                if (wikiContent != null && !wikiContent.isEmpty()) {
                    String pokemonId = UtilsFunctions.findPokemonId(multilingualNamesMap, pageTitle);
                    List<String[]> multilingualNames = pokemonId != null ? multilingualNamesMap.get(pokemonId) : null;
            
                    String infoboxContent = PokemonInfoboxRDFGenerator.extractInfobox(wikiContent, selectedInfoboxTypes);
            
                    if (infoboxContent != null) {
                        if (infoboxContent.contains("TCGPromoInfobox")) {
                            Model model = PokemonInfoboxRDFGenerator.generateRDFForTCGPromoInfobox(infoboxContent, pageTitle);
                            combinedModel.add(model);
                        } 
                        else if (infoboxContent.contains("Pokémon Infobox")) {
                            if (pokemonId != null && multilingualNames != null) {
                                Model model = PokemonInfoboxRDFGenerator.generateRDFForPokemonInfobox(infoboxContent, pageTitle, multilingualNames, pokemonId);
                                combinedModel.add(model);
                            } else {
                                System.out.println("ID Pokémon introuvable ou noms multilingues manquants pour la page : " + pageTitle);
                            }
                        } 
                        else {
                            System.out.println("Type d'infobox non reconnu pour la page : " + pageTitle);
                        }
                    } else {
                        System.out.println("L'infobox de la page est vide : " + pageTitle);
                    }
                } else {
                    System.out.println("Le contenu de la page est vide : " + pageTitle);
                }
            }
            

            try (FileWriter out = new FileWriter("pokemon_output.ttl")) {
                combinedModel.write(out, "TURTLE");
            }

            String fusekiEndpoint = "http://localhost:3030/semwebPokeDB";
            FusekiConnection.loadRDFIntoFuseki(combinedModel, fusekiEndpoint);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
