
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
		
		String proteinID = protID.getText();
		
		ObservableList<String> statList = FXCollections.observableArrayList();
		ObservableList<String> ecList = FXCollections.observableArrayList();
		ObservableList<String> domainsList = FXCollections.observableArrayList();
		ObservableList<String> neighbourList = FXCollections.observableArrayList();
		
		String avgJaccard;
		String minJaccard;
		String maxJaccard;
		String nbNeighbour;
		String avgDomains;
		String avgEc;
		
		ecList = Main.getEC(graphDb, proteinID);
		domainsList = Main.getDomains(graphDb, proteinID);
		neighbourList = Main.getNeighbour(graphDb, proteinID);
		avgJaccard = Main.getAvgJaccard(graphDb, proteinID);
		minJaccard = Main.getMinJaccard(graphDb, proteinID);
		maxJaccard = Main.getMaxJaccard(graphDb, proteinID);
		nbNeighbour = Main.getNbNeighbour(graphDb, proteinID);
		avgDomains = Main.getAvgDomains(graphDb, proteinID);
		avgEc = Main.getAvgEc(graphDb, proteinID);
		
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
		ecnumbers.setItems(ecList);
		domains.setItems(domainsList);
		neighbours.setItems(neighbourList);
		stats.setItems(statList);
		
	}
	
	private static ObservableList<String> getEC(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		ObservableList<String> ecList = FXCollections.observableArrayList();
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein) where p.id = \""+proteinID+"\" return p.ec";
		Result result = graphDb.execute(query, params);
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			String[] parts = concatFromArray(((String[]) row.get("p.ec"))).split(";");
			for (String part : parts) {
				ecList.add(part);
			}
			
		}
		
		tx.success();
		
		return ecList;
        }
	}
	
	private static ObservableList<String> getDomains(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		ObservableList<String> domainsList = FXCollections.observableArrayList();
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein) where p.id = \""+proteinID+"\" return p.domains";
		Result result = graphDb.execute(query, params);
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			String[] parts = concatFromArray(((String[]) row.get("p.domains"))).split(";");
			for (String part : parts) {
				domainsList.add(part);
			}
			
		}
		
		tx.success();
		
		return domainsList;
        }
	}
	
	private static ObservableList<String> getNeighbour(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		ObservableList<String> neighbourList = FXCollections.observableArrayList();
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return p2.id";
		Result result = graphDb.execute(query, params);
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			neighbourList.add(row.get("p2.id").toString());
			
		}
		
		tx.success();
		
		return neighbourList;
        }
	}
			

	private static String getAvgJaccard(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return avg(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			avgJaccard = row.get("avg(r.jaccard)").toString();
		}
		
		tx.success();
		
		return avgJaccard;
        }
			
	}
	
	
	private static String getMinJaccard(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String minJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return min(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			minJaccard = row.get("min(r.jaccard)").toString();
		}
		
		tx.success();
		
		return minJaccard;
        }
			
	}
	
	private static String getMaxJaccard(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String maxJaccard = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return max(r.jaccard)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			maxJaccard = row.get("max(r.jaccard)").toString();
		}
		
		tx.success();
		
		return maxJaccard;
        }
			
	}
	
	private static String getNbNeighbour(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String nbNeighbour = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return count(p)";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			nbNeighbour = row.get("count(p)").toString();
		}
		
		tx.success();
		
		return nbNeighbour;
        }
			
	}
	
	private static String getAvgDomains(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgDomains = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return avg(size(p2.domains))";
		Result result = graphDb.execute(query, params);
		
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			avgDomains = row.get("avg(size(p2.domains))").toString();
		}
		
		tx.success();
		
		return avgDomains;
        }
			
	}
	
	private static String getAvgEc(GraphDatabaseService graphDb, String proteinID) throws  IOException{
		
		try ( Transaction tx = graphDb.beginTx() ) {
		
		String avgEc = "0";
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (p:Protein)-[r:weight]-(p2) where p.id = \""+proteinID+"\" return avg(size(p2.ec))";
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
			result += i + ";";
		}
		return result;
	}



}
