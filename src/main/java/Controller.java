import static spark.Spark.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.*;

class Id{
    public int id;
};

class ResponseStatus{
    String state;
}

public class Controller {
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
        post("/browseAnnouncements/:name",(req, res) -> browseAnnouncements(req, res));
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
            if (categories.get(i).getName().equals(req.params("name"))){
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
    static Announcement createAnnouncement(Request req){
        Gson gson = new Gson();
        Announcement ann = gson.fromJson(req.body(), Announcement.class);
        saveAnnouncement(ann, ann.getCategory());
        return ann;
    }

    static void saveAnnouncement(Announcement a, Category cat){
        db.getAnnouncementsInCategories().get(cat).add(a);
    }

    static Announcement findAnnouncementByOwner(int userId, int announcementId){
        List<Announcement> announcements = db.getAnnouncements();
        for (Announcement a : announcements) {
            if (a.getOwner().getId() == userId && a.getId() == announcementId) return a;
        }
        return null;
    };

    static Announcement deleteAnnouncement(int userId, int announcementId){
        Announcement ann = findAnnouncementByOwner(userId, announcementId);
        if (ann != null) {
            db.getAnnouncementsInCategories().get(ann.getCategory()).remove(ann);
            return ann;
        }

        return null;
    }

    static Category findCategoryById(int categoryId) {
        List<Category> categories = db.getCategories();
        for (Category cat : categories) {
            if (cat.getId() == categoryId) return cat;
        }

        return null;
    }
    static List<Category> fetchCategories(){
        return db.getCategories();
    }

    static List<Announcement> fetchAnnouncements(int categoryId){
        List<Announcement> announcements = new ArrayList<>();

        for (Map.Entry<Category, List<Announcement>> entry : db.getAnnouncementsInCategories().entrySet()) {
            if (entry.getKey().getId() == categoryId) {
                announcements.addAll(entry.getValue());
                break;
            }
        }

        return announcements;
    }

    static List<Announcement> fetchUserWishlist(int userId){
        List<Announcement> announcementsWishList = new ArrayList<>();

        for (Map.Entry<User, List<Announcement>> entry : db.getWishList().entrySet()) {
            if (entry.getKey().getId() == userId) {
                announcementsWishList.addAll(entry.getValue());
                break;
            }
        }

        return announcementsWishList;
    }

    static String updateWishlist(int userId, int announcementId){
        db.getWishList().get(userId).add(db.getAnnouncements().get(announcementId));
        return "success";
    }

     static Announcement findAnnouncementById(int announcementId){
        List<Announcement> announcements = db.getAnnouncements(); // tu podobnie co funkcja powyżej
         for (Announcement a : announcements) {
             if (a.getId() == announcementId) return a;
         }

        return null;
    }

    static String addComment(int announcementId, int userId, String comment){
        Announcement announcement = findAnnouncementByOwner(userId, announcementId);
        List<String> comments = announcement.getComments();
        comments.add(comment);
        return "success";
    }

    static String updateAnnouncement(int announcementId, Announcement updatedData){
        Announcement announcement = findAnnouncementById(announcementId);
        announcement.setOwner(updatedData.getOwner());
        announcement.setContent(updatedData.getContent());
        announcement.setTitle(updatedData.getTitle());
        return "success";
    }

    static Category createCategory(Request req){
        Gson gson = new Gson();
        return gson.fromJson(req.body(), Category.class);
    }

    static Category deleteCategory(int categoryId){
        Category category = findCategoryById(categoryId);
        if (category!= null) {
            db.getCategories().remove(category);
        }

        return category;
    }


    static User findUserById(int userId){
        List<User> users = db.getUsers();
        for (User u : users) {
            if (u.getId() == userId) return u;
        }
        return null;
    }

    static String updateUserStatus(int userId, boolean status) {
        User u = findUserById(userId);
        if (u != null) {
            u.setBanStatus(status); //false - odbanowany, true - zbanowany
        }
        return "success";
    }

    static Statistics fetchStatistics (){
        return db.getStatistics();
    }
}


