import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {
    public static void main(String[] args) {
        //GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        File p = new File("/home/etudiants/stunault1u/TPBD/projetnosql/graph.db");
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(p);
        db.shutdown();

        System.out.println("ça marche!!!!!");
        try (Transaction tx = db.beginTx()) {
            // Perform DB operations
            System.out.println("ça marche 2");
            tx.success();
        }
    }
}