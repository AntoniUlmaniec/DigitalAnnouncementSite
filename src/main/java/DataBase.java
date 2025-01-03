import java.util.*;

public class DataBase {
    private HashMap<Category, List<Announcement>> announcementsInCategories;
    private List<User> users;
    private HashMap<User, List<Announcement>> wishList;
    private List<Category> categories;
    private List<Statistics> statistics;

    public DataBase() {
        announcementsInCategories = new HashMap<>();
        users = new ArrayList<>();
        wishList = new HashMap<>();
        categories = new ArrayList<>();
        statistics = new ArrayList<>();

        users.add(new User("tomek",1));

        Category cat = new Category();
        cat.setName("sport");
        categories.add(cat);

        Announcement announcement = new Announcement(1,"sprzedam opla","opel rocznik 2010, 100zł",users.get(0));
        List<Announcement> sportAnnouncements = new ArrayList<>();
        sportAnnouncements.add(announcement);
        announcementsInCategories.put(cat,sportAnnouncements);
    }

    public List<Announcement> getAnnouncements() {
        List<Announcement> allAnnouncements = new ArrayList<>();
        for (Map.Entry<Category, List<Announcement>> entry : announcementsInCategories.entrySet()) {
            allAnnouncements.addAll(entry.getValue());
        }

        return allAnnouncements;
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

    public HashMap<User, List<Announcement>> getWishList() { return wishList; }
}
