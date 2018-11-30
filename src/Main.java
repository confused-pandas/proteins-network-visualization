import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {

	public static void main(String[] args) {
		
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graphDb = dbFactory.newEmbeddedDatabase( new File("/home/etudiants/aghenda1u/Documents/NoSQL/neo4j-community-3.4.10/data/databases/graph.db") );
		
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
			
			// GET EC
			/*Map<String, Object> params = new HashMap<>();
			String query = "MATCH (p:Protein) RETURN p.domains;";
			Result result = graphDb.execute(query, params);
			System.out.println(result);		
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(concatFromArray(((String[]) row.get("p.domains"))));
			}*/
			
			
			Map<String, Object> params = new HashMap<>();
			String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return p,r,p2.id";
			Result result = graphDb.execute(query, params);
			System.out.println(result);		
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(row.get("p2.id"));
			}
			
			
            tx.success();
        }
		
		graphDb.shutdown();
	}
	
	static String concatFromArray(String[] a) {
		if (a == null) {
			return "";
		}
		String result="";
		for (String i : a) {
			result += i + " ";
		}
		return result;
	}

}
