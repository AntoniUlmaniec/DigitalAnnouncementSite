import java.util.*;

public class DataBase {
    private HashMap<Category, List<Announcement>> announcementsInCategories;
    private List<User> users;
    private HashMap<User, Announcement> wishList;
    private List<Category> categories;
    private List<Statistics> statistics;

    public DataBase() {
        announcementsInCategories = new HashMap<>();
        users = new ArrayList<>();
        wishList = new HashMap<>();
        categories = new ArrayList<>();
        statistics = new ArrayList<>();
    }

    public HashMap<Category, List<Announcement>> getAnnouncementsInCategories() {
        return announcementsInCategories;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public HashMap<User, Announcement> getWishList() { return wishList; }
}
