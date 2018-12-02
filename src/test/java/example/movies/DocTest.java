package example.movies;

import org.junit.Test;

import example.proteins.backend.ProteinService;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mh
 * @since 14.04.16
 */
public class DocTest {

    @Test
    public void testMovieFind() throws Exception {
        ProteinService service = new ProteinService("bolt://neo4j:1234@localhost");
        Map protein = service.findProtein("Q8QME4");
        assertEquals("Q8QME4", protein.get("title"));
        assertNotNull(protein.get("prot"));
    }
}
