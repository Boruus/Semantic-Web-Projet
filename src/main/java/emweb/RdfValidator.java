package emweb;

import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;

public class RdfValidator {
    public static void main(String[] args) {
        try {
            String SHAPES = "src/main/java/emweb/shapes.ttl"; 
            String DATA = "pokemon_output.ttl"; 
            
            Graph shapesGraph = RDFDataMgr.loadGraph(SHAPES);
            Graph dataGraph = RDFDataMgr.loadGraph(DATA);
            
            Shapes shapes = Shapes.parse(shapesGraph);
            
            ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
            
            ShLib.printReport(report);
            System.out.println();
            RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}