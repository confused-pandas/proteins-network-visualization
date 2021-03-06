package example.proteins.backend;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.setPort;

import example.proteins.util.Util;

/**
 * @author Najib Aghenda / Maxime Guyot / Guillaume Stunault
 */
public class ProteinServer {

    public static void main(String[] args) {
        setPort(Util.getWebPort());
        externalStaticFileLocation("src/main/webapp");
        final ProteinService service = new ProteinService(Util.getNeo4jUrl());
        new ProteinRoutes(service).init();
    }
}
