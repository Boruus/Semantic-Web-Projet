package emweb;

import org.apache.jena.rdf.model.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

            for (String pageTitle : combinedPages) {            
                String wikiContent = PokemonPagesFetcher.getWikiContent(pageTitle);
            
                if (wikiContent != null && !wikiContent.isEmpty()) {
                   /*  List<String> externalLinks = PokemonPagesFetcher.getExternalLinks(pageTitle);
                    System.out.println("externalLinks : " + externalLinks);
            
                    if (!externalLinks.isEmpty()) {
                        PokemonInfoboxRDFGenerator.addSameAsLinks(combinedModel, pageTitle, externalLinks);
                    }*/
                    //to do : add external links
            
                    String infoboxContent = PokemonInfoboxRDFGenerator.extractInfobox(wikiContent, selectedInfoboxTypes);
                    if (infoboxContent != null) {
                        Model model = PokemonInfoboxRDFGenerator.generateRDFForInfoboxType(infoboxContent, pageTitle);
                        combinedModel.add(model); 
                    } else {
                        System.out.println("L'infobox de la page est vide : " + pageTitle);
                    }
                } else {
                    System.out.println("Le contenu de la page est vide : " + pageTitle);
                }
            }
            
            //String tsvFilePath = "src/main/resources/pokedex-i18n.tsv";
            //PokemonInfoboxRDFGenerator.addMultilingualLabelsToModel(tsvFilePath, combinedModel);
            // to do : add triples from other sources

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
