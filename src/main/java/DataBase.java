import java.util.*;

public class DataBase {
    private HashMap<Category, List<Announcement>> announcementsInCategories;
    private List<User> users;
    private HashMap<User, List<Announcement>> wishList;
    private List<Category> categories;
    private Statistics statistics;

    static int userId = 1;
    static int announcementId = 1;
    static int categoryId = 1;

    public DataBase() {
        announcementsInCategories = new HashMap<>();
        users = new ArrayList<>();
        wishList = new HashMap<>();
        categories = new ArrayList<>();
        statistics = new Statistics();

        users.add(new User("tomek",userId));
        userId += 1;
        users.add(new User("olaf",userId));
        userId += 1;

        Category cat = new Category();
        cat.setName("sport");
        cat.setId(categoryId);
        categoryId+=1;
        categories.add(cat);

        Announcement announcement = new Announcement(announcementId,"sprzedam opla","opel rocznik 2010, 100zł",users.get(0), cat);
        announcementId+=1;
        Announcement announcement2 = new Announcement(announcementId,"sprzedam skocznie narciarską","opel rocznik 2010, 100zł",users.get(1), cat);
        announcementId+=1;

        List<Announcement> sportAnnouncements = new ArrayList<>();
        sportAnnouncements.add(announcement);
        sportAnnouncements.add(announcement2);
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

    public Statistics getStatistics() {
        return statistics;
    }

    public HashMap<User, List<Announcement>> getWishList() { return wishList; }
}
