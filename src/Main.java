import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {

	public static void main(String[] args) {
		
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graphDb = dbFactory.newEmbeddedDatabase( new File("/Users/aghenda1u/Downloads/neo4j-community-3.4.10/data/databases/graph.db") );
		
		try ( Transaction tx = graphDb.beginTx() )
        {
            
			/*
			// Database operations go here
			Map<String, Object> params = new HashMap<>();
			String query = "MATCH (n:Protein) RETURN n.id;";
			Result result = graphDb.execute(query, params);
			System.out.println(result);
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(row.get("n.id"));
			}
						
			// GET EC						
			Map<String, Object> params = new HashMap<>();
			String query = "MATCH (n:Protein) RETURN n.ec;";
			Result result = graphDb.execute(query, params);
			System.out.println(result);
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(row.get("n.ec"));
			}
			*/
			
			// GET DOMAINS
			Map<String, Object> params = new HashMap<>();
			String query = "MATCH (n:Protein) RETURN n.domains;";
			Result result = graphDb.execute(query, params);
			System.out.println(result);
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(row.get("n.domains"));
			}
			
			
            tx.success();
        }
		
		graphDb.shutdown();
	}

}
