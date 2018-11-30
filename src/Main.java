
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main extends Application {

	private static Pane root ;


	@FXML
	private TextField protID;
	@FXML
	private ListView ecnumbers;
	@FXML
	private ListView goterms;
	@FXML
	private ListView domains;
	@FXML
	private ListView neighbours;
	@FXML
	private ListView stats;

	@Override
	public void start(Stage primaryStage) throws Exception{
		root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
		primaryStage.setTitle("Neo4Prot");
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();
	}

	@FXML
	private void runapp(ActionEvent e) throws IOException {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graphDb = dbFactory.newEmbeddedDatabase( new File("C:\\neo4j\\data\\databases\\graph.db") );
		
		ObservableList<String> statList = FXCollections.observableArrayList();
		
		String avgJaccard;
		String minJaccard;
		String maxJaccard;
		String nbNeighbour;
		String avgDomains;
		String avgEc;
		
		avgJaccard = Main.getAvgJaccard(graphDb);
		minJaccard = Main.getMinJaccard(graphDb);
		maxJaccard = Main.getMaxJaccard(graphDb);
		nbNeighbour = Main.getNbNeighbour(graphDb);
		avgDomains = Main.getAvgDomains(graphDb);
		avgEc = Main.getAvgEc(graphDb);
		
		statList.add("Average Jaccard Coefficient");
		statList.add("---| "+avgJaccard);
		statList.add("");
		statList.add("Minimum Jaccard Coefficient");
		statList.add("---| "+minJaccard);
		statList.add("");
		statList.add("Maximal Jaccard Coefficient");
		statList.add("---| "+maxJaccard);
		statList.add("");
		statList.add("Number of neighbour");
		statList.add("---| "+nbNeighbour);
		statList.add("");
		statList.add("Average number of domains");
		statList.add("---| "+avgDomains);
		statList.add("");
		statList.add("Average number of EC");
		statList.add("---| "+avgEc);

		graphDb.shutdown();

		ObservableList<String> items = FXCollections.observableArrayList();
		for (int i = 0; i<5; i++) {
			items.add("test");
		}
		ecnumbers.setItems(items);
		stats.setItems(statList);
	}

	private static String getAvgJaccard(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return avg(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			avgJaccard = row.get("avg(r.jaccard)").toString();
		}
		
		tx.success();
		
		return avgJaccard;
        }
			
	}
	
	
	private static String getMinJaccard(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String minJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return min(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			minJaccard = row.get("min(r.jaccard)").toString();
		}
		
		tx.success();
		
		return minJaccard;
        }
			
	}
	
	private static String getMaxJaccard(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String maxJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return max(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			maxJaccard = row.get("max(r.jaccard)").toString();
		}
		
		tx.success();
		
		return maxJaccard;
        }
			
	}
	
	private static String getNbNeighbour(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String nbNeighbour = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return count(p)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			nbNeighbour = row.get("count(p)").toString();
		}
		
		tx.success();
		
		return nbNeighbour;
        }
			
	}
	
	private static String getAvgDomains(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgDomains = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return avg(size(p2.domains))";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			avgDomains = row.get("avg(size(p2.domains))").toString();
		}
		
		tx.success();
		
		return avgDomains;
        }
			
	}
	
	private static String getAvgEc(GraphDatabaseService graphDb) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgEc = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \"P08393\" return avg(size(p2.ec))";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			avgEc = row.get("avg(size(p2.ec))").toString();
		}
		
		tx.success();
		
		return avgEc;
        }
			
	}

	public static void main(String[] args) throws IOException {

/*
		try ( Transaction tx = graphDb.beginTx() )
        {
            

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

			
			// GET EC

			Map<String, Object> params = new HashMap<>();
			String query = "MATCH (n:Protein) RETURN n.domains;";
			Result result = graphDb.execute(query, params);
			System.out.println(result);
		
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
				System.out.println(concatFromArray(((String[]) row.get("n.domains"))));
			}

			
            tx.success();
        }
		
		graphDb.shutdown();
	*/

		launch(args);
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
