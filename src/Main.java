
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
		primaryStage.setTitle("DrugMediator");
		primaryStage.setScene(new Scene(root, 900, 600));
		primaryStage.show();
	}

	@FXML
	private void runapp(ActionEvent e) throws IOException {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService graphDb = dbFactory.newEmbeddedDatabase( new File("C:\\neo4j\\neo4j-community-3.4.10\\data\\databases/graph.db") );

		Main.rr(graphDb);

		graphDb.shutdown();

		ObservableList<String> items = FXCollections.observableArrayList();
		for (int i = 0; i<5; i++) {
			items.add("test");
		}
		ecnumbers.setItems(items);
	}

	private static void rr(GraphDatabaseService graphDb) throws  IOException{
		Map<String, Object> params = new HashMap<>();
		String query = "MATCH (n:Protein) RETURN n.domains;";
		Result result = graphDb.execute(query, params);
		System.out.println(result);

		while(result.hasNext()) {
			Map<String, Object> row = result.next();
			System.out.println(concatFromArray(((String[]) row.get("n.domains"))));
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
