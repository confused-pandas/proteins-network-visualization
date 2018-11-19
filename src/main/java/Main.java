import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {
    public static void main(String[] args) {
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        File p = new File("./graph.db");
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(p);

        try (Transaction tx = db.beginTx()) {
            // Perform DB operations
            tx.success();
        }
    }
}