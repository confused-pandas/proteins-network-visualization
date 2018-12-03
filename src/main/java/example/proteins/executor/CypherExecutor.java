package example.proteins.executor;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Najib Aghenda / Maxime Guyot / Guillaume Stunault
 */
public interface CypherExecutor {
    Iterator<Map<String,Object>> query(String statement, Map<String,Object> params);
}
