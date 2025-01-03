import static spark.Spark.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Id{
    public int id;
};

class ResponseStatus{
    String state;
}

public class System {
    static DataBase db = new DataBase();

    public static void main(String[] args) {
        staticFiles.location("/static");
        port(4000);

        post("/loadUsers",(req,res) -> loadUsers(req,res));
        post("/login",(req,res) -> login(req,res));
//        post("/publishAnnouncement",(req, res) -> publishAnnouncement(req, res)); // na razie losowe rzeczy, nie zwracaj uwagi na nazwy
//        post("/editAnnouncement",(req, res) -> editAnnouncement(req, res));
//        post("/deleteAnnouncement",(req, res) -> deleteAnnouncement(req, res));
        post("/browseCategories",(req, res) -> browseCategories(req, res));
        post("/browseAnnouncements",(req, res) -> browseAnnouncements(req, res));
//        post("/addToWishlist",(req, res) -> addToWishlist(req, res));
//        post("/commentOnAnnouncement",(req, res) -> commentOnAnnouncement(req, res));
//        post("/addCategory",(req, res) -> addCategory(req, res));
//        post("/removeCategory",(req, res) -> removeCategory(req, res));
//        post("/banUser",(req, res) -> banUser(req, res));
//        post("/viewGeneralStatistics",(req, res) -> viewGeneralStatistics(req, res));

    }

    // obsługa endpointów (komunikacja strona - system)
    static String loadUsers(Request req, Response res){
        Gson gson = new Gson();
        res.type("application/json");
        return gson.toJson(db.getUsers());
    }

    static String login(Request req, Response res){
        Gson gson = new Gson();
        Id id = gson.fromJson(req.body(), Id.class);
        User u = findUserById(id.id);

        res.type("application/json");
        ResponseStatus state = new ResponseStatus();
        if (u != null){
            state.state = "logged";
            return gson.toJson(state);
        }
        state.state = "not logged";
        return gson.toJson(state);
    }
//
//    static String publishAnnouncement(Request req, Response res){
//        // req.body() musi zawierać także informację o kategorii ogłoszenia
//        String success = saveAnnouncement(createAnnouncement(req));
//        return success;
//    }
//
//    static String editAnnouncement(Request req, Response res){
//        Gson gson = new Gson();
//        Announcement a = gson.fromJson(req.body(), Announcement.class);
//        updateAnnouncement(a.getId(), a);
//        return "zedytowano ogłoszenie"; // roboczo zwraca prostego stringa
//    }
//
//    static String deleteAnnouncement(Request req, Response res){
//        return "usunięto ogłoszenie"; // roboczo zwraca prostego stringa
//    }
//
    static String browseCategories(Request req, Response res){
        Gson gson = new Gson();
        res.type("application/json");
        return gson.toJson(db.getCategories());
    }
//
    static String browseAnnouncements(Request req, Response res){
        Gson gson = new Gson();
        res.type("application/json");
        List<Category>categories = db.getCategories();
        Category category = null;
        for (int i = 0; i < categories.size(); i++){
            if (categories.get(i).getName() == req.params("name")){
                category = categories.get(i);
            }
        }

        if (category == null) {
            ResponseStatus state = new ResponseStatus();
            state.state = req.params().toString();
            return gson.toJson(state);
        }

        List<Announcement> announcements = db.getAnnouncementsInCategories().get(category);
        return gson.toJson(announcements); // roboczo zwraca prostego stringa
    }
//
//    static String addToWishlist(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//    static String commentOnAnnouncement(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//    static String addCategory(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//    static String removeCategory(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//    static String banUser(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//    static String viewGeneralStatistics(Request req, Response res){
//        return "wyświetlono kategorie"; // roboczo zwraca prostego stringa
//    }
//
//
//
//    // obsługa bazy danych (system - baza danych)
//    static Announcement createAnnouncement(Request req){
//        Gson gson = new Gson();
//        Announcement ann = gson.fromJson(req.body(), Announcement.class);
//        return ann;
//    }
//
//    static String saveAnnouncement(Announcement a){
//        //do dogrania jak to robimy
//        return "success";
//    }
//
//    static Announcement findAnnouncementByOwner(int userId, int announcementId){
//        // do dogrania jak to robimy
//        return new Announcement();
//    };
//
//    static Announcement deleteAnnouncement(int announcementId){
//        // do dogrania jak to robimy
//        return new Announcement();
//    }
//
//    static List<Category> fetchCategories(){
//        return db.getCategories();
//    }
//
//    static List<Announcement> fetchAnnouncements(int categoryId){
//        HashMap<Category, List<Announcement>> announcementsInCategories = db.getAnnouncementsInCategories();
//        return announcementsInCategories.get(categoryId);
//    }
//
//    static ArrayList<Announcement> fetchUserWishlist(int userId){
//        return db.getWishList().get(userId); // typy różne, do przegadania
//    }
//
//    static String updateWishlist(int userId, int announcementId){
//        // do zastanowienia, przydałaby się lista z samymi announcementami?
//        // db.getWishList().get(userId).add(db.getAnnouncements().get(announcementId));
//        return "success";
//    }
//
    static Announcement findAnnouncementById(int announcementId){
        List<Announcement> announcements = db.getAnnouncements(); // tu podobnie co funkcja powyżej
        for(int i = 0; i < announcements.size(); i++) {
            Announcement a = announcements.get(i);
            if (a.getId() == announcementId) return a;
        }

        return null;
    }
//
//    static String addComment(int announcementId, int userId, String comment){
//        // do zastanowienia
//        return "success";
//    }
//
//    static String updateAnnouncement(int announcementId, Announcement updatedData){
//        Announcement b = findAnnouncementById(announcementId);
//        b.setOwner(updatedData.getOwner());
//        b.setContent(updatedData.getContent());
//        b.setTitle(updatedData.getTitle());
//        return "success";
//    }
//
//    static String deleteAnnouncement(int announcementId, String updatedData){
//        // db.getAnnouncements().remove(db.getAnnouncements().get(announcementId)); jak wyżej
//        return "success";
//    }
//
//    static Category createCategory(Request req){
//        Gson gson = new Gson();
//        return gson.fromJson(req.body(), Category.class);
//    }
//
//    static Category deleteCategory(int categoryId){
//        return db.getCategories().remove(categoryId);
//    }
//
//
    static User findUserById(int userId){
        List<User> users = db.getUsers();
        for(int i = 0; i < users.size(); i++){
            User u = users.get(i);
            if(u.getId()==userId) return u; //potrzeba getterów i setterów w klasie User
        }
        return null;
    }
//
//    static String updateUserStatus(int userId, String status){
//        // w klasie user potrzebne pole 'status', najlepiej typu String
//        User u = findUserById(userId);
//        u.setStatus(status); //potrzebne także gettery i settery do tego pola
//        return u.getStatus();
//    }
//
//    static List<Statistics> fetchStatistics (int userId, String status){ //pole statistics w DataBase ma być typu List<Statistics>?
//        if(findUserById(userId) instanceof Admin){
//            return db.getStatistics();
//        } else return null;
//    }
//
//    static String processStatistics(List<Statistics> stats) {
//        // potrzebna metoda toString() w klasie Statistics
//        String result = "";
//        for (int i = 0; i < stats.size(); i++){
//            result+= stats.get(i).toString();
//        }
//
//        return result;
//    }


}


