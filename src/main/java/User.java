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

    public void browseAnnouncements(int categoryId) {
        List<Announcement> announcements = Controller.fetchAnnouncements(categoryId);
        for (Announcement announcement : announcements) {
            System.out.println(announcement);
        }
    }

    public int getNumberOfAnnouncements() {
        return numberOfAnnouncements;
    }

    public void setNumberOfAnnouncements(int numberOfAnnouncements) {
        this.numberOfAnnouncements = numberOfAnnouncements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isBanStatus() {
        return banStatus;
    }

    public void setBanStatus(boolean banStatus) {
        this.banStatus = banStatus;
    }

    public List<Announcement> getWishList() {
        return wishList;
    }

    public void setWishList(List<Announcement> wishList) {
        this.wishList = wishList;
    }
}
