package example.proteins.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.net.URLDecoder;

import static spark.Spark.get;

public class ProteinRoutes implements SparkApplication {

    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private ProteinService service;

    public ProteinRoutes(ProteinService service) {
        this.service = service;
    }

    public void init() {
        get("/protein/:id", (req, res) -> gson.toJson(service.findProtein(URLDecoder.decode(req.params("id")))));
        get("/search", (req, res) -> gson.toJson(service.search(req.queryParams("q"))));
        get("/graph/:id", (req, res) -> {
            int limit = req.queryParams("limit") != null ? Integer.valueOf(req.queryParams("limit")) : 100;

            return gson.toJson(service.graph(limit, req.params("id")));
        });
        System.out.println(gson.toJson(service.graph(100, "K9N7C7")));
    }
}
