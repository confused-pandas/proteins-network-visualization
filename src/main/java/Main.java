import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {
    public static void main(String[] args) {
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase("C:/TPNeo4jDB");

        try (Transaction tx = db.beginTx()) {
            // Perform DB operations
            tx.success();
        }
    }
}