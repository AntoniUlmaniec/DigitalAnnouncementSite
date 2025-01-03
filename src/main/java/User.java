import java.util.ArrayList;
import java.util.List;
public class User {
    private int numberOfAnnouncements;
    private List<Announcement> wishList;
    private String username;
    private int id;
    private boolean banStatus;

    public User(String username, int id) {
        this.numberOfAnnouncements = 0;
        this.wishList = new ArrayList<Announcement>();
        this.username = username;
        this.id = id;
        this.banStatus = false;
    }

    public void publishAnnouncement(Announcement announcement) {
        numberOfAnnouncements++;
        announcement.setOwner(this);

        // do dokonczenia
    }

    public void editAnnouncement(int announcementId, String updatedData) {
        Announcement announcement = findAnnouncementById(announcementId);

        if (announcement.getOwner().equals(this)) {
            announcement.setContent(updatedData);
//            System.out.println("Announcement edited: " + announcement.getTitle());
        } else {
//            System.out.println("You are not the owner of the announcement.");
        }
    }

    public void deleteAnnouncement(int announcementId) {
        Announcement announcement = findAnnouncementById(announcementId);

        if (announcement.getOwner().equals(this)) {
            // tu trzeba usunac to ogloszenie z bazy danych
//            System.out.println("Announcement deleted: " + announcement.getTitle());
        } else {
//            System.out.println("You are not the owner of the announcement.");
        }
    }

    public void addToWishList(Announcement announcement) {
        wishList.add(announcement);
//        System.out.println("Announcement added to wishlist: " + announcement.getTitle());
    }

    public void commentOnAnnouncement(Announcement announcement, String comment) {
    }

    private Announcement findAnnouncementById(int id) {
        // tutaj przeszukuje baze danych

        return new Announcement();  // zwraca to ogloszenie
    }

    public List<Category> browseCategories() {
        // do dokoczenia
        return new ArrayList<>();
    }

    public List<Announcement> browseAnnouncements(int categoryId) {
        // do dokonczenia
        return new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBanStatus() {
        return banStatus;
    }
    public void setBanStatus(boolean banStatus) {
        this.banStatus = banStatus;
    }
}
