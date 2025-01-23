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
