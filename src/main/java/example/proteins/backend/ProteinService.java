package example.proteins.backend;

import org.neo4j.helpers.collection.Iterators;

import example.proteins.executor.BoltCypherExecutor;
import example.proteins.executor.CypherExecutor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 * @author Najib Aghenda / Maxime Guyot / Guillaume Stunault
 */
public class ProteinService {

    private final CypherExecutor cypher;

    public ProteinService(String uri) {
        cypher = createCypherExecutor(uri);
    }

    private CypherExecutor createCypherExecutor(String uri) {
        try {
            String auth = new URL(uri.replace("bolt","http")).getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new BoltCypherExecutor(uri,"neo4j","galathea30");
            }
            return new BoltCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public Map findProtein(String id) {
        if (id==null) return Collections.emptyMap();
        return Iterators.singleOrNull(cypher.query(
                "MATCH (protein:Protein {id:{id}})" +
                " RETURN protein.id as id, collect({ec:protein.ec, domains:protein.domains}) as prot LIMIT 1",
                map("id", id)));
    }

    @SuppressWarnings("unchecked")
    public Iterable<Map<String,Object>> search(String query) {
        if (query==null || query.trim().isEmpty()) return Collections.emptyList();
        return Iterators.asCollection(cypher.query(
        		"MATCH (protein:Protein)-[r:weight]-(protein2)" +
                " WHERE protein.id = {protID} \n" +
                " RETURN protein, avg(r.jaccard) as avg_jac, min(r.jaccard) as min_jac, max(r.jaccard) as max_jac, count(protein) as nbProt, avg(size(protein2.domains)) as avgSizeDom, avg(size(protein2.ec)) as avgSizeEC",
                map("protID", query)));
    }
    
    @SuppressWarnings("unchecked")
    public Iterable<Map<String,Object>> createChart(String id) {
        return Iterators.asCollection(cypher.query(
        		"MATCH (protein:Protein)-[r:weight]-(protein2)" +
                " WHERE protein.id = {protID} \n" +
                " RETURN r.jaccard as jac",
                map("protID", id)));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> graph(int limit, String id) {
        Iterator<Map<String,Object>> result = cypher.query(
                "MATCH (protein:Protein)-[a:linkedTo]-(d:Domains)-[b:linkedTo]-(protein2:Protein) " +
                "WHERE protein.id = {id} \n" +
                " RETURN protein.id as pid, collect(d.value) as ip, protein2.id as p2id " +
                " LIMIT {limit}", map("limit",limit, "id", id));
        System.out.println("ID-->"+id);
        List nodes = new ArrayList();
        List rels= new ArrayList();
        int i=0;
        nodes.add(map("id",result.next().get("pid"),"label","protein"));
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(map("id",row.get("p2id"),"label","protein"));
            int target=i;
            i++;
            for (Object interpro : (Collection) row.get("ip")) {
                Map<String, Object> domains = map("id", interpro, "label", "domains");
                int source = nodes.indexOf(domains);
                if (source == -1) {
                    nodes.add(domains);
                    source = i++;
                }
                rels.add(map("source",source,"target",target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
}
