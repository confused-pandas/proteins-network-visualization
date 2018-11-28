import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {

	public static void main(String[] args) {
		
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graphDb = dbFactory.newEmbeddedDatabase( new File("./resources/graph.db") );
		
		try ( Transaction tx = graphDb.beginTx() )
        {
            // Database operations go here
            tx.success();
        }
		
		graphDb.shutdown();
	}

}
