import static spark.Spark.*;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.*;

class Id {
    public int id;
};

class ResponseStatus {
    String state;
}

public class Controller {
    static DataBase db = new DataBase();

    public static void main(String[] args) {
        staticFiles.location("/static");
        port(4000);

        post("/loadUsers", (req, res) -> loadUsers(req, res));
        post("/login", (req, res) -> login(req, res));
        post("/fetchMyAnouncements/:userId", (req, res) -> displayUserAnnouncements(req, res));
        post("/publishAnnouncement", (req, res) -> publishAnnouncement(req, res)); // na razie losowe rzeczy, nie zwracaj uwagi na nazwy
        post("/editAnnouncement", (req, res) -> editAnnouncement(req, res));
        post("/getAnnouncement/:announcementId", (req, res) -> getAnnouncement(req, res));
        post("/deleteAnnouncement/:id", (req, res) -> deleteAnnouncement(req, res));
        post("/browseCategories", (req, res) -> browseCategories(req, res));
        post("/browseAnnouncements/:name", (req, res) -> browseAnnouncements(req, res));
        post("/addToWishlist/:id", (req, res) -> addToWishlist(req, res));
        post("/commentOnAnnouncement/:id", (req, res) -> commentOnAnnouncement(req, res));
        post("/addCategory", (req, res) -> addCategory(req, res));
        post("/removeCategory", (req, res) -> removeCategory(req, res));
        post("/banUser", (req, res) -> banUser(req, res));
        post("/viewGeneralStatistics", (req, res) -> viewGeneralStatistics(req, res));
        post("/fetchWishlist/:userId", (req, res) -> fetchWishlist(req, res));
        post("/fetchBannedUsers", (req, res) -> fetchBannedUsers(req, res));
        post("/unbanUser", (req, res) -> unbanUser(req, res));

    }

    // obsługa endpointów (komunikacja strona - system)
    static String loadUsers(Request req, Response res) {
        Gson gson = new Gson();
        res.type("application/json");
        return gson.toJson(db.getUsers());
    }

    static String login(Request req, Response res) {
        Gson gson = new Gson();
        Id id = gson.fromJson(req.body(), Id.class);
        User u = findUserById(id.id);

        res.type("application/json");
        ResponseStatus state = new ResponseStatus();
        if (u != null) {
            state.state = "logged";
            if (u.getClass() == Admin.class) {
                state.state = "admin";
            }
            return gson.toJson(state);
        }

        state.state = "not logged";
        return gson.toJson(state);
    }

    static String displayUserAnnouncements(Request req, Response res) {
        Gson gson = new Gson();
        int userId = Integer.parseInt(req.params("userId"));
        ArrayList<Announcement> userAnnouncements = new ArrayList<>();
        List<Announcement> allAnnouncements = db.getAnnouncements();
        List<Announcement> wishlist = fetchUserWishlist(userId);

        for (Announcement ann : allAnnouncements) {
            if (ann.getOwner().getId() == userId) {
                ann.setFavorite(wishlist.contains(ann));
                userAnnouncements.add(ann);
            }
        }
        return gson.toJson(userAnnouncements);
    }

    static String publishAnnouncement(Request req, Response res) {
        Gson gson = new Gson();
        // req.body() musi zawierać także informację o kategorii ogłoszenia
        return gson.toJson(createAnnouncement(req));
    }

    static String editAnnouncement(Request req, Response res) {
        Gson gson = new Gson();
        Announcement a = gson.fromJson(req.body(), Announcement.class);
        String response = updateAnnouncement(a.getId(), a);
        ResponseStatus resp = new ResponseStatus();
        resp.state = response;
        return gson.toJson(resp);
    }

    static String getAnnouncement(Request req, Response res) {
        Gson gson = new Gson();
        int announcementId = Integer.parseInt(req.params("announcementId"));
        return gson.toJson(findAnnouncementById(announcementId));
    }

    static String deleteAnnouncement(Request req, Response res) {
        Gson gson = new Gson();
        ResponseStatus resp = new ResponseStatus();

        int announcementId = Integer.parseInt(req.params("id"));
        Announcement announcement = findAnnouncementById(announcementId);
        List<Announcement> ann = db.getAnnouncementsInCategories().get(findAnnouncementById(announcementId).getCategory());
        ann.remove(announcement);
        db.getWishList().forEach((user, wishlist) -> wishlist.remove(announcement));

        resp.state = "success";
        return gson.toJson(resp);
    }

    //
    static String browseCategories(Request req, Response res) {
        Gson gson = new Gson();
        res.type("application/json");
        return gson.toJson(db.getCategories());
    }

    //
    static String browseAnnouncements(Request req, Response res) {
        Gson gson = new Gson();
        res.type("application/json");
        List<Category> categories = db.getCategories();
        Category category = null;
        int userId = Integer.parseInt(req.queryParams("userId"));
        List<Announcement> wishlist = fetchUserWishlist(userId);

        for (Category cat : categories) {
            if (cat.getName().equals(req.params("name"))) {
                category = cat;
                break;
            }
        }

        if (category == null) {
            ResponseStatus state = new ResponseStatus();
            state.state = "Kategoria nie istnieje";
            return gson.toJson(state);
        }

        List<Announcement> announcements = db.getAnnouncementsInCategories().get(category);
        for (Announcement ann : announcements) {
            ann.setFavorite(wishlist.contains(ann));
        }

        return gson.toJson(announcements);
    }


    static String addToWishlist(Request req, Response res) {
        Gson gson = new Gson();
        ResponseStatus data = gson.fromJson(req.body(), ResponseStatus.class);
        int userId = Integer.parseInt(req.params("id"));
        return updateWishlist(userId, Integer.parseInt(data.state)); // roboczo zwraca prostego stringa
    }

    static String commentOnAnnouncement(Request req, Response res) {
        Gson gson = new Gson();
        ResponseStatus data = gson.fromJson(req.body(), ResponseStatus.class);
        int announcementId = Integer.parseInt(req.params("id"));
        findAnnouncementById(announcementId).getComments().add(data.state);

        ResponseStatus resp = new ResponseStatus();
        resp.state = "success";
        return gson.toJson(resp);
    }

    static String addCategory(Request req, Response res) {
        Gson gson = new Gson();
        return gson.toJson(createCategory(req));
    }

    static String removeCategory(Request req, Response res) {
        Gson gson = new Gson();
        Id id = gson.fromJson(req.body(), Id.class);
        return gson.toJson(deleteCategory(id.id));
    }

    static String banUser(Request req, Response res) {
        Gson gson = new Gson();
        Id id = gson.fromJson(req.body(), Id.class);
        int userId = id.id;

        User user = findUserById(userId);
        ResponseStatus resp = new ResponseStatus();

        if (user != null) {
            user.setBanStatus(true);
            List<User> users = db.getUsers();
            List<User> bannedUsers = db.getBannedUsers();
            bannedUsers.add(user);
            users.remove(user);

            List<Announcement> allAnnouncements = db.getAnnouncements();
            allAnnouncements.removeIf(announcement -> announcement.getOwner().getId() == userId);

            db.getAnnouncementsInCategories().values().forEach(announcements ->
                    announcements.removeIf(announcement -> announcement.getOwner().getId() == userId)
            );

            db.getWishList().forEach((key, wishlist) ->
                    wishlist.removeIf(announcement -> announcement.getOwner().getId() == userId)
            );

            resp.state = "success";
        }

        return gson.toJson(resp);
    }


    static String viewGeneralStatistics(Request req, Response res) {
        Gson gson = new Gson();
        Statistics stats = new Statistics(db.getUsers().size(), db.getBannedUsers().size(), db.getAnnouncements().size());
        return gson.toJson(stats); // roboczo zwraca prostego stringa
    }


    // obsługa bazy danych (system - baza danych)
    static Announcement createAnnouncement(Request req) {
        Gson gson = new Gson();
        Announcement ann = gson.fromJson(req.body(), Announcement.class);
        ann.setId(DataBase.announcementId);
        DataBase.announcementId++;

        ann.setOwner(findUserById(ann.getOwner().getId()));
        ann.setCategory(findCategoryByName(ann.getCategory().getName()));

        saveAnnouncement(ann, ann.getCategory());
        return ann;
    }

    static void saveAnnouncement(Announcement a, Category cat) {
        db.getAnnouncementsInCategories().get(cat).add(a);
    }

    static Announcement findAnnouncementByOwner(int userId, int announcementId) {
        List<Announcement> announcements = db.getAnnouncements();
        for (Announcement a : announcements) {
            if (a.getOwner().getId() == userId && a.getId() == announcementId) return a;
        }
        return null;
    }

    ;

    static Announcement deleteAnnouncement(int userId, int announcementId) {
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

    static Category findCategoryByName(String categoryName) {
        List<Category> categories = db.getCategories();
        for (Category cat : categories) {
            if (cat.getName().equals(categoryName)) return cat;
        }

        return null;
    }

    static List<Category> fetchCategories() {
        return db.getCategories();
    }

    static List<Announcement> fetchAnnouncements(int categoryId) {
        List<Announcement> announcements = new ArrayList<>();

        for (Map.Entry<Category, List<Announcement>> entry : db.getAnnouncementsInCategories().entrySet()) {
            if (entry.getKey().getId() == categoryId) {
                announcements.addAll(entry.getValue());
                break;
            }
        }

        return announcements;
    }

    static List<Announcement> fetchUserWishlist(int userId) {
        List<Announcement> announcementsWishList = new ArrayList<>();

        for (Map.Entry<User, List<Announcement>> entry : db.getWishList().entrySet()) {
            if (entry.getKey().getId() == userId) {
                announcementsWishList.addAll(entry.getValue());
                break;
            }
        }

        return announcementsWishList;
    }

    static String updateWishlist(int userId, int announcementId) {
        Gson gson = new Gson();
        User u = findUserById(userId);
        List<Announcement> wishlist = db.getWishList().get(u);
        Announcement ann = findAnnouncementById(announcementId);
        if (wishlist == null) {
            wishlist = new ArrayList<>();
            wishlist.add(ann);
            db.getWishList().put(u, wishlist);
            return gson.toJson(wishlist);
        }

        if (!wishlist.contains(ann)) {
            wishlist.add(ann);
        } else {
            wishlist.remove(ann);
        }

        return gson.toJson(wishlist);
    }

    static Announcement findAnnouncementById(int announcementId) {
        List<Announcement> announcements = db.getAnnouncements(); // tu podobnie co funkcja powyżej
        for (Announcement a : announcements) {
            if (a.getId() == announcementId) return a;
        }

        return null;
    }

    static String addComment(int announcementId, int userId, String comment) {
        Announcement announcement = findAnnouncementByOwner(userId, announcementId);
        List<String> comments = announcement.getComments();
        comments.add(comment);
        return "success";
    }

    static String updateAnnouncement(int announcementId, Announcement updatedData) {
        Announcement announcement = findAnnouncementById(announcementId);
        announcement.setContent(updatedData.getContent());
        announcement.setTitle(updatedData.getTitle());
        return "success";
    }

    static Category createCategory(Request req) {
        Gson gson = new Gson();
        Category newCat = gson.fromJson(req.body(), Category.class);
        newCat.setId(DataBase.categoryId++);
        db.getCategories().add(newCat);
        db.getAnnouncementsInCategories().put(newCat, new ArrayList<>());
        return newCat;
    }

    static Category deleteCategory(int categoryId) {
        Category category = findCategoryById(categoryId);
        if (category != null) {
            db.getAnnouncementsInCategories().remove(category); // usuwamy wszystkie ogłoszenia z danej kategorii
            db.getCategories().remove(category);
        }

        return category;
    }


    static User findUserById(int userId) {
        List<User> users = db.getUsers();
        for (User u : users) {
            if (u.getId() == userId) return u;
        }
        List<User> bannedUsers = db.getBannedUsers();
        for (User u : bannedUsers) {
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

    static String fetchWishlist(Request req, Response res) {
        Gson gson = new Gson();
        int userId = Integer.parseInt(req.params("userId"));
        List<Announcement> wishlist = fetchUserWishlist(userId);

        for (Announcement ann : wishlist) {
            ann.setFavorite(true);
        }

        return gson.toJson(wishlist);
    }

    static String fetchBannedUsers(Request req, Response res) {
        Gson gson = new Gson();
        List<User> bannedUsers = db.getBannedUsers();

        return gson.toJson(bannedUsers);
    }

    static String unbanUser(Request req, Response res) {
        Gson gson = new Gson();
        Id id = gson.fromJson(req.body(), Id.class);
        int userId = id.id;

        User user = findUserById(userId);
        ResponseStatus resp = new ResponseStatus();

        if (user != null && user.isBanStatus()) {
            user.setBanStatus(false);
            List<User> users = db.getUsers();
            List<User> bannedUsers = db.getBannedUsers();
            bannedUsers.remove(user);
            users.add(user);

            resp.state = "success";
        }

        return gson.toJson(resp);
    }

    static Statistics fetchStatistics() {
        return db.getStatistics();
    }
}


