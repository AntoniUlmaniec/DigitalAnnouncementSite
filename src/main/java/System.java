import static spark.Spark.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import java.util.ArrayList;

public class System {




    public static void main(String[] args) {
        staticFiles.location("/static");
        port(4000);

        post("/add",(req, res) -> addCar(req, res)); // na razie losowe rzeczy, nie zwracaj uwagi na nazwy
    }

    static String addCar(Request req, Response res){
        return "hihi";
    }
}


