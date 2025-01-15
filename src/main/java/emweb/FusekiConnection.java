package emweb;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

public class FusekiConnection {
    
    public static void loadRDFIntoFuseki(Model model, String fusekiEndpoint) {
        try (RDFConnection conn = RDFConnectionFactory.connect(fusekiEndpoint)) {
            conn.load(model); 
            System.out.println("RDF data loaded into Fuseki at " + fusekiEndpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
