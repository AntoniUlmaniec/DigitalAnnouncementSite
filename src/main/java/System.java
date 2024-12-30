import static spark.Spark.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class System {
    static DataBase db = new DataBase();


    public static void main(String[] args) {
        staticFiles.location("/static");
        port(4000);

        post("/publishAnnouncement",(req, res) -> publishAnnouncement(req, res)); // na razie losowe rzeczy, nie zwracaj uwagi na nazwy
        post("/editAnnouncement",(req, res) -> editAnnouncement(req, res));
        post("/deleteAnnouncement",(req, res) -> deleteAnnouncement(req, res));
        post("/browseCategories",(req, res) -> browseCategories(req, res));
        post("/browseAnnouncements",(req, res) -> browseAnnouncements(req, res));
        post("/addToWishlist",(req, res) -> addToWishlist(req, res));
        post("/commentOnAnnouncement",(req, res) -> commentOnAnnouncement(req, res));
        post("/addCategory",(req, res) -> addCategory(req, res));
        post("/removeCategory",(req, res) -> removeCategory(req, res));
        post("/banUser",(req, res) -> banUser(req, res));
        post("/viewGeneralStatistics",(req, res) -> viewGeneralStatistics(req, res));

    }

    // obsługa endpointów (komunikacja strona - system)
    static String publishAnnouncement(Request req, Response res){
        String success = save(create(req));
        return success;
    }

    static String editAnnouncement(Request req, Response res){
        return "zedytowano ogłoszenie"; // roboczo zwraca prostego stringa
    }

    static String deleteAnnouncement(Request req, Response res){
        return "usunięto ogłoszenie"; // roboczo zwraca prostego stringa
    }

    static String browseCategories(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String browseAnnouncements(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String addToWishlist(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String commentOnAnnouncement(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String addCategory(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String removeCategory(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String banUser(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }

    static String viewGeneralStatistics(Request req, Response res){
        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
    }



    // obsługa bazy danych (system - baza danych)
    static Announcement create(Request req){
        Gson gson = new Gson();
        Announcement ann = gson.fromJson(req.body(), Announcement.class);
        return ann;
    }

    static String save(Announcement a){
        //do dogrania jak to robimy
        return "success";
    }

    static Announcement findAnnouncementByOwner(int userId, int announcementId){
        // do dogrania jak to robimy
        return new Announcement();
    };

    static Announcement deleteAnnouncement(int announcementId){
        // do dogrania jak to robimy
        return new Announcement();
    }

    static List<Category> fetchCategories(){
        return db.getCategories();
    }

    static List<Announcement> fetchAnnouncements(int categoryId){
        HashMap<Category, List<Announcement>> announcementsInCategories = db.getAnnouncementsInCategories();
        return announcementsInCategories.get(categoryId);
    }

    static ArrayList<Announcement> fetchUserWishlist(int userId){
        return db.getWishList().get(userId); // typy różne, do przegadania
    }

    static String updateWishlist(int userId, int announcementId){
        // do zastanowienia, przydałaby się lista z samymi announcementami?
        // db.getWishList().get(userId).add(db.getAnnouncements().get(announcementId));
        return "success";
    }

    static Announcement findAnnouncementById(int announcementId){
        //return db.getAnnouncements().get(announcementId); // tu podobnie co funkcja powyżej
    }

    static String addComment(int announcementId, int userId, String comment){
        // do zastanowienia
        return "success";
    }
}


